package com.example.administrator.recorddemo.bean;

import android.graphics.drawable.AnimationDrawable;

/**
 * Created by Administrator on 2018/9/12.
 */
public class RecordBean {
    public String type;//类型
    public String time;//单位s
    public String path;//本地缓存
    public String url;//网络缓存
    public String key;//传给后台的key
    public boolean animIsFinished = true;//动画是否结束
    public AnimationDrawable mAnim;//动画对象

    public RecordBean setTime(String time) {
        this.time = time;
        return this;
    }

    public RecordBean setPath(String path) {
        this.path = path;
        return this;
    }

    public RecordBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public RecordBean setType(String type) {
        this.type = type;
        return this;
    }

    public RecordBean setKey(String key) {
        this.key = key;
        return this;
    }
}
