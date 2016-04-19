package com.example.sunxiaodong.library.bean;

import android.view.View;
import android.view.ViewGroup;

import com.example.sunxiaodong.library.widget.EmotionPageView;

import java.util.List;

/**
 * 表情页实体(代表一页），基础之上增加了显示行列数
 * Created by sunxiaodong on 16/4/15.
 */
public class EmotionPageEntity<T> extends PageEntity<EmotionPageEntity> {

    private List<T> mEmotionList;//表情数据源，T是具体的表情数据项
    private int mRowNum;//每页行数
    private int mColumnNum;//每页列数
    private DelBtnStatus mDelBtnStatus;//删除按钮状态

    public List<T> getEmotionList() {
        return mEmotionList;
    }

    public void setEmotionList(List<T> mEmotionList) {
        this.mEmotionList = mEmotionList;
    }

    public int getRowNum() {
        return mRowNum;
    }

    public void setRowNum(int mRowNum) {
        this.mRowNum = mRowNum;
    }

    public int getColumnNum() {
        return mColumnNum;
    }

    public void setColumnNum(int mColumnNum) {
        this.mColumnNum = mColumnNum;
    }

    public DelBtnStatus getDelBtnStatus() {
        return mDelBtnStatus;
    }

    public void setDelBtnStatus(DelBtnStatus mDelBtnStatus) {
        this.mDelBtnStatus = mDelBtnStatus;
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position, EmotionPageEntity pageEntity) {
        if (mPageViewInstantiateListener != null) {
            return mPageViewInstantiateListener.instantiateItem(container, position, this);
        }
        if (getRootView() == null) {
            EmotionPageView pageView = new EmotionPageView(container.getContext());
            pageView.setNumColumns(mColumnNum);
            setRootView(pageView);
        }
        return getRootView();
    }

    /**
     * 删除按钮的状态
     */
    public enum DelBtnStatus {
        // 0,1,2
        GONE, FOLLOW, LAST;

        public boolean isShow() {
            return !GONE.toString().equals(this.toString());
        }
    }

}
