package com.example.sunxiaodong.library.bean;

import com.example.sunxiaodong.library.interfaces.PageViewInstantiateListener;

import java.util.ArrayList;

/**
 * 表情集数据和视图实体，创建器
 * Created by sunxiaodong on 16/4/15.
 */
public class EmotionPageSetEntity<T> extends PageSetEntity<EmotionPageEntity> {

    final int mColumnNum;
    final int mRowNum;
    final EmotionPageEntity.DelBtnStatus mDelBtnStatus;
    final ArrayList<T> mEmotionList;

    public EmotionPageSetEntity(final Builder builder) {
        super(builder);
        this.mColumnNum = builder.columnNum;
        this.mRowNum = builder.rowNum;
        this.mDelBtnStatus = builder.delBtnStatus;
        this.mEmotionList = builder.emotionList;
    }

    public static class Builder<T> extends PageSetEntity.Builder {

        protected int columnNum;//每页行数
        protected int rowNum;//每页列数
        protected EmotionPageEntity.DelBtnStatus delBtnStatus = EmotionPageEntity.DelBtnStatus.GONE;
        protected ArrayList<T> emotionList;//表情集数据源

        protected PageViewInstantiateListener pageViewInstantiateListener;//初始化 表情显示页面视图 接口

        public Builder() {
        }

        public Builder setColumnNum(int columnNum) {
            this.columnNum = columnNum;
            return this;
        }

        public Builder setRowNum(int rowNum) {
            this.rowNum = rowNum;
            return this;
        }

        public Builder setDelBtnStatus(EmotionPageEntity.DelBtnStatus delBtnStatus) {
            this.delBtnStatus = delBtnStatus;
            return this;
        }

        public Builder setEmotionList(ArrayList<T> emotionList) {
            this.emotionList = emotionList;
            return this;
        }

        public Builder setIPageViewInstantiateItem(PageViewInstantiateListener pageViewInstantiateListener) {
            this.pageViewInstantiateListener = pageViewInstantiateListener;
            return this;
        }

        public Builder setIsShowIndicator(boolean isShowIndicator) {
            this.isShowIndicator = isShowIndicator;
            return this;
        }

        public Builder setEmotionIconUri(String emotionIconUri) {
            this.emotionIconUri = emotionIconUri;
            return this;
        }

        public Builder setEmotionSetName(String emotionSetName) {
            this.emotionSetName = emotionSetName;
            return this;
        }

        public EmotionPageSetEntity<T> build() {
            int emoticonSetSum = emotionList.size();
            int del = delBtnStatus.isShow() ? 1 : 0;
            int everyPageMaxSum = columnNum * rowNum - del;
            pageCount = (int) Math.ceil((double) emotionList.size() / everyPageMaxSum);
            int start = 0;
            int end = everyPageMaxSum > emoticonSetSum ? emoticonSetSum : everyPageMaxSum;
            if (!pageEntityList.isEmpty()) {
                pageEntityList.clear();
            }
            for (int i = 0; i < pageCount; i++) {
                EmotionPageEntity emotionPageEntity = new EmotionPageEntity();
                emotionPageEntity.setColumnNum(columnNum);
                emotionPageEntity.setRowNum(rowNum);
                emotionPageEntity.setDelBtnStatus(delBtnStatus);
                emotionPageEntity.setEmotionList(emotionList.subList(start, end));
                emotionPageEntity.setIPageViewInstantiateItem(pageViewInstantiateListener);
                pageEntityList.add(emotionPageEntity);
                start = everyPageMaxSum + i * everyPageMaxSum;
                end = everyPageMaxSum + (i + 1) * everyPageMaxSum;
                if (end >= emoticonSetSum) {
                    end = emoticonSetSum;
                }
            }
            return new EmotionPageSetEntity<>(this);
        }
    }

}
