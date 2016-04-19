package com.example.sunxiaodong.library.interfaces;

/**
 * Created by sunxiaodong on 16/4/15.
 */
public interface EmotionClickListener<T> {

    void onEmotionClick(T t, int actionType, boolean isDelBtn);

}