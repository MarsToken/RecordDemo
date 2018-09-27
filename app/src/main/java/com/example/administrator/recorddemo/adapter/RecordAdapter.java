package com.example.administrator.recorddemo.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;

import com.example.administrator.recorddemo.R;
import com.example.administrator.recorddemo.bean.RecordBean;
import com.example.administrator.recorddemo.interfaces.MediaPlayerListener;

import java.util.List;

/**
 * Created by Administrator on 2018/9/12.
 */
public class RecordAdapter extends BaseRecyclerViewAdapter<RecordBean> {
    private MediaPlayerListener mMediaPlayerListener;

    public RecordAdapter(Context context, List<RecordBean> beans, MediaPlayerListener listener) {
        super(context, beans);
        mMediaPlayerListener = listener;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.activity_main_item_header;
        } else if (viewType == 1) {
            return R.layout.activity_main_item_child;
        }
        return 0;
    }

    @Override
    protected void onBindDataToView(ViewHolder holder, RecordBean recordBean) {
        if (recordBean.type.equals("cell2")) {
            holder.setText(R.id.tv_record_time, recordBean.time);
            holder.setClickListener(R.id.rl_record);
            holder.setClickListener(R.id.btn_delete);
            recordBean.mAnim = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.anim_common_record);
            holder.getView(R.id.iv_anim).setBackgroundDrawable(recordBean.mAnim);
            if (recordBean.animIsFinished) {
                if (recordBean.mAnim.isRunning()) {
                    recordBean.mAnim.selectDrawable(0);
                    recordBean.mAnim.stop();
                }
            } else {
                if (!recordBean.mAnim.isRunning()) {
                    recordBean.mAnim.start();
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        RecordBean bean = mBeans.get(position);
        if ("cell1".equals(bean.type)) {//标题
            return 0;
        } else if ("cell2".equals(bean.type)) {
            return 1;
        }
        return -1;
    }

    @Override
    protected void onSingleViewClick(View view, int position) {
        switch (view.getId()) {
            case R.id.rl_record:
                //播放网络，或本地
                RecordBean bean = mBeans.get(position);
                if (!bean.animIsFinished) {
                    mMediaPlayerListener.stop(bean);
                    bean.mAnim.selectDrawable(0);
                    bean.mAnim.stop();
                } else {
                    mMediaPlayerListener.play(mBeans.get(position));
                    mBeans.get(position).mAnim.start();
                    //终止其余的动画
                    for (int i = 0; i < mBeans.size(); i++) {
                        if (position != i) {
                            if (null != mBeans.get(i).mAnim) {
                                mBeans.get(i).animIsFinished = true;
                                mBeans.get(i).mAnim.selectDrawable(0);
                                mBeans.get(i).mAnim.stop();
                            }
                        }
                    }
                }
                break;
            case R.id.btn_delete:
                mMediaPlayerListener.cancel(mBeans.get(position));
                break;
        }
    }

    public void stopAnim(RecordBean bean) {
        bean.mAnim.selectDrawable(0);
        bean.mAnim.stop();
        //notifyDataSetChanged();
    }
}
