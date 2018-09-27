package com.example.administrator.recorddemo.interfaces;

import com.example.administrator.recorddemo.bean.RecordBean;

/**
 * Created by Administrator on 2018/9/12.
 */
public interface MediaPlayerListener {
    void play(RecordBean bean);

    void stop(RecordBean bean);

    void cancel(RecordBean bean);
}
