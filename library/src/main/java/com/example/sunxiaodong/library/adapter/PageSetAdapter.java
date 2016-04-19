package com.example.sunxiaodong.library.adapter;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunxiaodong.library.bean.PageEntity;
import com.example.sunxiaodong.library.bean.PageSetEntity;

import java.util.ArrayList;

/**
 * 表情集适配器，所有表情集的页面数据使用同一个适配器
 */
public class PageSetAdapter extends PagerAdapter {

    private final ArrayList<PageSetEntity> mPageSetEntityList = new ArrayList<>();//所有表情集数据列表

    public void add(PageSetEntity pageSetEntity) {
        add(mPageSetEntityList.size(), pageSetEntity);
    }

    public void add(int index, PageSetEntity pageSetEntity) {
        if (pageSetEntity == null) {
            return;
        }
        mPageSetEntityList.add(index, pageSetEntity);
    }

    public void add(View view) {
        add(mPageSetEntityList.size(), view);
    }

    public void add(int index, View view) {
        PageSetEntity pageSetEntity = new PageSetEntity.Builder()
                .addPageEntity(new PageEntity(view))
                .setShowIndicator(false)
                .build();
        mPageSetEntityList.add(index, pageSetEntity);
    }

    public PageSetEntity get(int position) {
        return mPageSetEntityList.get(position);
    }

    private PageEntity getPageEntity(int position) {
        for (PageSetEntity pageSetEntity : mPageSetEntityList) {
            if (pageSetEntity.getPageCount() > position) {
                return (PageEntity) pageSetEntity.getPageEntityList().get(position);
            } else {
                position -= pageSetEntity.getPageCount();
            }
        }
        return null;
    }

    public void remove(int position) {
        mPageSetEntityList.remove(position);
        notifyData();
    }

    public ArrayList<PageSetEntity> getPageSetEntityList() {
        return mPageSetEntityList;
    }

    /**
     * 获取传入表情集的起始位置
     *
     * @param pageSetEntity 表情集
     * @return 返回传入表情集的起始位置
     */
    public int getPageSetStartPosition(PageSetEntity pageSetEntity) {
        if (pageSetEntity == null || TextUtils.isEmpty(pageSetEntity.getEmotionSetId())) {
            return 0;
        }
        int startPosition = 0;
        for (int i = 0; i < mPageSetEntityList.size(); i++) {
            if (i == mPageSetEntityList.size() - 1 && !pageSetEntity.getEmotionSetId().equals(mPageSetEntityList.get(i).getEmotionSetId())) {
                //比较到最后一个表情集 都没有找到该表情集位置，则返回0
                return 0;
            }
            if (pageSetEntity.getEmotionSetId().equals(mPageSetEntityList.get(i).getEmotionSetId())) {
                return startPosition;
            }
            startPosition += mPageSetEntityList.get(i).getPageCount();
        }
        return startPosition;
    }

    public void notifyData() {
    }

    @Override
    public int getCount() {
        //所有表情集的页面数累加返回
        int count = 0;
        for (PageSetEntity pageSetEntity : mPageSetEntityList) {
            count += pageSetEntity.getPageCount();
        }
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getPageEntity(position).instantiateItem(container, position, null);//每一页表情
        if (view == null) {
            return null;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
