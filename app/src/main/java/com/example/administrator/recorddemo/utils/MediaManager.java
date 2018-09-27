package com.example.administrator.recorddemo.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.administrator.recorddemo.utils.network.NetUtil;

import java.io.IOException;

/**
 * 播音控制器
 */
public class MediaManager implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    public MediaPlayer mPlayer;
    public boolean isPause;
    private Context mContext;
    private String url;
    private SVProgressHUD mHud;
    /**
     * 此handler是为了播放时倒数计时，UI如果不需要可以删掉
     */
    private Handler mHandler;
    private boolean isFinished;
    private boolean isPrepared;
    public int audioDuration;


    public MediaManager(Context mContext, SVProgressHUD svProgressHUD, Handler handler) {
        this.mHud = svProgressHUD;
        this.mContext = mContext;
        this.mHandler = handler;
    }

    /**
     * 加载本地资源
     *
     * @param path
     * @param onCompletionListener
     */
    public void playSound(String path, OnCompletionListener onCompletionListener) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setOnErrorListener((mp, what, extra) -> {
                mPlayer.reset();
                return false;
            });
        } else {
            mPlayer.reset();
        }

        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            if (null != mHandler) {
                mHandler.sendMessage(Message.obtain(mHandler, 2, 1));
            }
            Log.e("tag", "time=" + mPlayer.getDuration());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载网络资源
     */
    public void play(String url) {
        try {
            this.url = url;
            mPlayer = new MediaPlayer();
            mPlayer.setLooping(false);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setDataSource(mContext, Uri.parse(url));
            mHud.show();
            mPlayer.prepareAsync();

        } catch (IOException e) {
            Log.v("AudioHttpPlayer", e.getMessage());
        }
    }


    public boolean isPlaying() {
        if (mPlayer != null && isPrepared) {
            return mPlayer.isPlaying();
        }
        return false;
    }


    //停止函数
    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    //继续
    public void resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
            isPause = false;
            if (null != mHandler) {
                mHandler.postDelayed(r, 1000);
            }
        }
    }


    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (r != null && null != mHandler) {
            mHandler.removeCallbacks(r);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("main", "  MediaManager====  onCompletion");
        mediaStop();
    }

    public void mediaStop() {
        isFinished = true;
        mPlayer.seekTo(0);
        mPlayer.reset();
        if (null != mHandler) {
            mHandler.removeCallbacks(r);
            mHandler.sendMessage(Message.obtain(mHandler, 2, 0));
        }
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mHud.isShowing()) {
            mHud.dismissImmediately();
        }
        mPlayer.stop();
        mPlayer.release();
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(mContext, "播放失败", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        mediaStart(mp);
    }

    public void mediaStart(MediaPlayer mp) {
        if (mHud.isShowing()) {
            mHud.dismissImmediately();
        }
        Log.i("main", "mediaStart== " + mp.getDuration());
        audioDuration = mp.getDuration();
        mp.seekTo(0);
        mp.start();
        if (null != mHandler) {
            mHandler.removeCallbacks(r);
            mHandler.sendMessage(Message.obtain(mHandler, 2, 1));
            mHandler.postDelayed(r, 1000);
        }
    }

    private Runnable r = new Runnable() {

        @Override
        public void run() {
            if (isPlaying()) {
                Log.i("main", mPlayer.getCurrentPosition() + "  ====   " + mPlayer.getDuration());
                if (null != mHandler) {
                    mHandler.sendMessage(Message.obtain(mHandler, 1, mPlayer.getCurrentPosition()));
                    mHandler.postDelayed(r, 1000);
                }
            }
        }
    };


    public void play() {
        //首次播放缓存至本地，录音后替换
        if (!isPrepared) {     //如果已经播放过了，不在检查联网
            if (NetUtil.netIsAble(mContext) == -1) {
                return;
            }
        }
        if (mPlayer != null) {
            if (!mPlayer.isPlaying()) {
                if (!isFinished) {// 如果onCompletion()后直接开始/ stop后 准备在开始
                    mHud.show();
                    mPlayer.prepareAsync();
                } else {
                    mediaStart(mPlayer);
                }
            } else {
                isFinished = false;
                mPlayer.stop();
                if (null != mHandler) {
                    mHandler.removeCallbacks(r);
                    mHandler.sendMessage(Message.obtain(mHandler, 2, 0));
                }
            }
        }
    }


    public void stop() {
        isPrepared = false;
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            //mPlayer.release();
            if (null != mHandler) {
                mHandler.removeCallbacks(r);
                mHandler.sendMessage(Message.obtain(mHandler, 2, 0));
            }
        }
    }

    public void destory() {
        if (mPlayer != null) {
            if (isPrepared && mPlayer.isPlaying()) {
                mPlayer.stop();
                if (null != mHandler) {
                    mHandler.removeCallbacks(r);
                    mHandler.sendMessage(Message.obtain(mHandler, 2, 0));
                }
            }
            mPlayer.release();
        }
    }
}
