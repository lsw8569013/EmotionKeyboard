package com.example.sunxiaodong.library.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

/**
 * 表情集数据和视图实体
 * @param <T>
 */
public class PageSetEntity<T extends PageEntity> implements Serializable {

    protected final String mEmotionSetId = UUID.randomUUID().toString();//表情集标识
    protected final boolean mIsShowIndicator;//是否显示指示器
    protected final LinkedList<T> mPageEntityList;//该表情集显示的各页面视图
    protected final String mEmotionIconUri;//表情操作项图标地址
    protected final String mEmotionSetName;//表情集名字

    public PageSetEntity(final Builder builder) {
        this.mIsShowIndicator = builder.isShowIndicator;
        this.mPageEntityList = builder.pageEntityList;
        this.mEmotionIconUri = builder.emotionIconUri;
        this.mEmotionSetName = builder.emotionSetName;
    }

    public String getEmotionSetId() {
        return mEmotionSetId;
    }

    public boolean isShowIndicator() {
        return mIsShowIndicator;
    }

    public LinkedList<T> getPageEntityList() {
        return mPageEntityList;
    }

    public String getEmotionIconUri() {
        return mEmotionIconUri;
    }

    public String getEmotionSetName() {
        return mEmotionSetName;
    }

    public int getPageCount() {
        return mPageEntityList == null ? 0 : mPageEntityList.size();
    }

    public static class Builder<T extends PageEntity> {

        protected int pageCount;
        protected boolean isShowIndicator = true;
        protected LinkedList<T> pageEntityList = new LinkedList<>();
        protected String emotionIconUri;
        protected String emotionSetName;

        public Builder setPageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder setShowIndicator(boolean showIndicator) {
            isShowIndicator = showIndicator;
            return this;
        }

        public Builder setPageEntityList(LinkedList<T> pageEntityList) {
            this.pageEntityList = pageEntityList;
            return this;
        }

        public Builder addPageEntity(T pageEntity) {
            pageEntityList.add(pageEntity);
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

        public Builder() {
        }

        public PageSetEntity<T> build() {
            return new PageSetEntity<>(this);
        }
    }

}
