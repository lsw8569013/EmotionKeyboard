package com.example.sunxiaodong.library.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.example.sunxiaodong.library.bean.PageEntity;

/**
 * 初始化 表情显示页面视图 接口
 * @param <T>
 */
public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);

}
