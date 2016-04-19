package com.example.sunxiaodong.library.interfaces;

import android.view.ViewGroup;

import com.example.sunxiaodong.library.adapter.EmotionAdapter;

public interface EmotionDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmotionAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
