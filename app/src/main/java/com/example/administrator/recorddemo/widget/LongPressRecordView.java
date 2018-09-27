package com.example.administrator.recorddemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.administrator.recorddemo.interfaces.OnLongPressListener;

/**
 * Created by wangmaobo on 2018/9/14.
 */
public class LongPressRecordView extends android.support.v7.widget.AppCompatTextView {
    private OnGestureListener mListener;

    public LongPressRecordView(Context context) {
        this(context, null, -1);
    }

    public LongPressRecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LongPressRecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(OnGestureListener listener) {
        mListener = listener;
    }

    private void init() {
        GestureDetector gesture = new GestureDetector(new RecordLongClickGesture().setLongPressListener(new OnLongPressListener() {
            @Override
            public void startRecord() {
                if (null != mListener) {
                    mListener.longClick();
                }
            }
        }));
        setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (null != mListener) {
                    mListener.actionUp();
                }
            }
            return gesture.onTouchEvent(motionEvent);
        });
        setFocusable(true);
        setClickable(true);
        setLongClickable(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    public interface OnGestureListener {
        void longClick();

        void actionUp();
    }
}
