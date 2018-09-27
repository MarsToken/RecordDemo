package com.example.administrator.recorddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.administrator.recorddemo.adapter.RecordAdapter;
import com.example.administrator.recorddemo.bean.RecordBean;
import com.example.administrator.recorddemo.interfaces.MediaPlayerListener;
import com.example.administrator.recorddemo.interfaces.RecordPermissionListener;
import com.example.administrator.recorddemo.utils.MediaManager;
import com.example.administrator.recorddemo.utils.SharedPreferencesUtil;
import com.example.administrator.recorddemo.utils.network.API;
import com.example.administrator.recorddemo.utils.network.FileUploadObserver;
import com.example.administrator.recorddemo.utils.network.MultipartBuilder;
import com.example.administrator.recorddemo.utils.network.RetrofitFactory;
import com.example.administrator.recorddemo.utils.network.UploadFileRequestBody;
import com.example.administrator.recorddemo.widget.LongPressRecordView;
import com.example.administrator.recorddemo.utils.RecorderManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECORD = 0x111;
    //录音控制器
    private RecorderManager mp3Recorder;
    //播音控制器
    private MediaManager mMediaManager;
    //UI
    private SVProgressHUD mHud;
    private RecyclerView mRecyclerView;
    private RecordAdapter mAdapter;
    LongPressRecordView btn;
    //Data
    private List<RecordBean> mList = new ArrayList<>();
    //Judgement
    private boolean hasRecordRight;
    private boolean isNewRecord;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    Log.e("tag", "msg.obj=" + msg.obj);
                    mp3Recorder.setTime(msg.obj.toString() + "＂");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (null != savedInstanceState) {
//            Intent intent = new Intent(this, StartActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    private void clear() {
        mMediaManager.release();
        if (null != mp3Recorder) {
            mp3Recorder.stop();
            mp3Recorder = null;
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private void init() {
        btn = findViewById(R.id.btn_record);
        btn.setListener(new LongPressRecordView.OnGestureListener() {
            @Override
            public void longClick() {
                AndPermission
                        .with(MainActivity.this)
                        .runtime()
                        .permission(Permission.Group.MICROPHONE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Log.e("tag", "有录音权限");
                                startRec();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Toast.makeText(MainActivity.this, "录音权限获取失败", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .rationale(new Rationale<List<String>>() {
                            @Override
                            public void showRationale(Context context, List<String> data, RequestExecutor executor) {
                                executor.execute();
                                //executor.cancel();
                            }
                        })
                        .start();
            }

            @Override
            public void actionUp() {
                if (hasRecordRight) {
                    endRec();
                }
            }
        });
        mHud = new SVProgressHUD(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mMediaManager = new MediaManager(this, mHud, mHandler);
        mAdapter = new RecordAdapter(this, mList, new MediaPlayerListener() {
            @Override
            public void play(RecordBean bean) {
                bean.animIsFinished = false;
                mMediaManager.playSound(bean.path, mediaPlayer -> {
                    Log.e("tag", "finished");
                    stop(bean);
                });
            }

            @Override
            public void stop(RecordBean bean) {
                bean.animIsFinished = true;
                mMediaManager.stop();
                mAdapter.stopAnim(bean);
            }

            @Override
            public void cancel(RecordBean bean) {
                File file = new File(bean.path);
                if (file.exists()) {
                    Log.e("tag", "发送网络请求，删除文件！");
                    file.delete();
                }
                mList.remove(bean);//也要删除本地文件
                mAdapter.notifyDataSetChanged();
                //mp3Recorder.deleteRecordFile();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mList.add(new RecordBean().setType("cell1"));
        traverse();
        mAdapter.notifyDataSetChanged();
    }

    public void traverse() {
        File root = new File(RecorderManager.mPath);
        File[] files = root.listFiles();
        if (null != files) {
            for (File f : files) {
                String time = SharedPreferencesUtil.getSetting(this, f.getAbsolutePath(), "0");
                mList.add(new RecordBean().setType("cell2").setPath(f.getAbsolutePath()).setTime(time));
            }
        }
    }

    private void endRec() {
        if (isNewRecord) {
            btn.setText("按住 说话");
            mp3Recorder.stop();
            mHandler.removeCallbacks(r);
            upLoadRecord();
            rTime = 0;
            SharedPreferencesUtil.setSetting(this, mp3Recorder.getAbsolutePath(), mp3Recorder.getTime());
            mList.add(new RecordBean()
                    .setPath(mp3Recorder.getAbsolutePath())
                    .setTime(mp3Recorder.getTime())
                    .setType("cell2")
                    .setKey(mp3Recorder.getKey()));
            mAdapter.notifyDataSetChanged();
            //上传逻辑
            upLoadFile(API.url + mp3Recorder.getKey(), mp3Recorder.mRecorderFile, new FileUploadObserver<ResponseBody>() {
                @Override
                public void onUploadSuccess(ResponseBody responseBody) {
                    if (responseBody == null) {
                        Log.e("tag", "responseBody null");
                        return;
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        //ArrayList<String> fileIds = new ArrayList<String>();
                        //fileIds.add(jsonObject.getString("fileId"));
                        Log.e("tag", jsonObject.getString("Result"));
                        Log.e("tag", jsonObject.getString("ResultMsg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onUploadFail(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onProgress(int progress) {
                    Log.e("tag", "progress=" + progress);
                }
            });
        }
    }

    private void upLoadRecord() {
        if (!mp3Recorder.mRecorderFile.exists()) {
            Log.e("main", "no exists!");
        } else {
            Log.e("tag", "record success!");
        }
    }

    private void startRec() {
        isNewRecord = true;
        btn.setText("松开 结束");
        mp3Recorder = RecorderManager.getInstance(System.currentTimeMillis() + "", new RecordPermissionListener() {
            @Override
            public void hasRecordRight() {
                hasRecordRight = true;
                mHandler.post(r);
            }

            @Override
            public void noRecordRight() {
                hasRecordRight = false;
                btn.setText("按住 说话");
                Toast.makeText(MainActivity.this, "您无录音权限", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            mp3Recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int rTime;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            mHandler.sendMessage(Message.obtain(mHandler, 3, rTime));
            rTime++;
            mHandler.removeCallbacks(r);
            mHandler.postDelayed(r, 1000);
        }
    };

    private void upLoadFile(String url, File file,
                            FileUploadObserver<ResponseBody> fileUploadObserver) {
        API api = RetrofitFactory.getInstance();
        UploadFileRequestBody uploadFileRequestBody =
                new UploadFileRequestBody(file, fileUploadObserver);
        api.uploadFile(url, MultipartBuilder.fileToMultipartBody(file,
                uploadFileRequestBody))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);

    }
}
