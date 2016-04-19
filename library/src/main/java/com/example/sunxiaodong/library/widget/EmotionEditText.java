package com.example.sunxiaodong.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunxiaodong on 16/4/14.
 */
public class EmotionEditText extends EditText {

    private List<EmotionFilter> mFilterList;

    private OnBackKeyClickListener mOnBackKeyClickListener;
    private OnSizeChangedListener mOnSizeChangedListener;

    public EmotionEditText(Context context) {
        this(context, null);
    }

    public EmotionEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(oldh > 0 && mOnSizeChangedListener != null){
            mOnSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            super.setText(text, type);
        } catch (ArrayIndexOutOfBoundsException e) {
            setText(text.toString());
        }
    }

    @Override
    protected final void onTextChanged(CharSequence arg0, int start, int lengthBefore, int after) {
        super.onTextChanged(arg0, start, lengthBefore, after);
        if(mFilterList == null){
            return;
        }
        for(EmotionFilter emoticonFilter : mFilterList) {
            emoticonFilter.filter(this, arg0, start, lengthBefore, after);
        }
    }

    public void addEmotionFilter(EmotionFilter emoticonFilter){
        if(mFilterList == null){
            mFilterList = new ArrayList<>();
        }
        mFilterList.add(emoticonFilter);
    }

    public void removedEmotionFilter(EmotionFilter emoticonFilter){
        if(mFilterList != null && mFilterList.contains(emoticonFilter)){
            mFilterList.remove(emoticonFilter);
        }
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if(mOnBackKeyClickListener != null){
            mOnBackKeyClickListener.onBackKeyClick();
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setOnBackKeyClickListener(OnBackKeyClickListener onBackKeyClickListener) {
        mOnBackKeyClickListener = onBackKeyClickListener;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        mOnSizeChangedListener = onSizeChangedListener;
    }

    /**
     * 点击返回键，回调
     */
    public interface OnBackKeyClickListener {
        void onBackKeyClick();
    }

    /**
     * 输入框大小改变回调
     */
    public interface OnSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

}
