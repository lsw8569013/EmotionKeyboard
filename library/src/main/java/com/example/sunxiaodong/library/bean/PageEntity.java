package com.example.sunxiaodong.library.bean;

import android.view.View;
import android.view.ViewGroup;

import com.example.sunxiaodong.library.interfaces.PageViewInstantiateListener;

/**
 * 表情页实体，代表显示的一页表情
 * @param <T>
 */
public class PageEntity<T extends PageEntity> implements PageViewInstantiateListener<T> {

    protected View mRootView;

    protected PageViewInstantiateListener mPageViewInstantiateListener;

    public PageEntity() {
    }

    public PageEntity(View view) {
        this.mRootView = view;
    }

    public View getRootView() {
        return mRootView;
    }

    public void setRootView(View rootView) {
        this.mRootView = rootView;
    }

    public void setIPageViewInstantiateItem(PageViewInstantiateListener pageViewInstantiateListener) {
        this.mPageViewInstantiateListener = pageViewInstantiateListener;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position, T pageEntity) {
        if (mPageViewInstantiateListener != null) {
            return mPageViewInstantiateListener.instantiateItem(container, position, this);
        }
        return getRootView();
    }
}
