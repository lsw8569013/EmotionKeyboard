package com.example.sunxiaodong.library.widget.emotion;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.example.sunxiaodong.library.adapter.PageSetAdapter;
import com.example.sunxiaodong.library.bean.PageSetEntity;

/**
 * 表情页展示ViewPager
 * Created by sunxiaodong on 16/4/13.
 */
public class EmotionDisplayView extends ViewPager {

    protected PageSetAdapter mPageSetAdapter;//表情页适配器
    protected int mCurrentPagePosition;//当前显示表情页位置

    private OnEmotionPageChangedListener mOnEmotionPageChangedListener;

    public EmotionDisplayView(Context context) {
        super(context);
    }

    public EmotionDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(PageSetAdapter adapter) {
        super.setAdapter(adapter);
        this.mPageSetAdapter = adapter;
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                emotionPageChange(position);
                mCurrentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mOnEmotionPageChangedListener == null || mPageSetAdapter.getPageSetEntityList().isEmpty()) {
            return;
        }
        PageSetEntity pageSetEntity = mPageSetAdapter.getPageSetEntityList().get(0);
        mOnEmotionPageChangedListener.playTo(0, pageSetEntity);
        mOnEmotionPageChangedListener.emotionSetChanged(pageSetEntity);
    }

    /**
     * 表情页面改变
     *
     * @param position 滑动到的表情页位置
     */
    public void emotionPageChange(int position) {
        if (mPageSetAdapter == null) {
            return;
        }
        int end = 0;
        for (PageSetEntity pageSetEntity : mPageSetAdapter.getPageSetEntityList()) {
            int size = pageSetEntity.getPageCount();
            if (end + size > position) {
                //从前到后，逐个检测表情集，找到position所在的表情集
                boolean isEmotionSetChanged = true;//表情集是否改变
                if (mCurrentPagePosition - end >= size) {
                    // 滑到了上一表情集
                    if (mOnEmotionPageChangedListener != null) {
                        mOnEmotionPageChangedListener.playTo(position - end, pageSetEntity);
                    }
                } else if (mCurrentPagePosition - end < 0) {
                    // 滑到了下一表情集
                    if (mOnEmotionPageChangedListener != null) {
                        mOnEmotionPageChangedListener.playTo(0, pageSetEntity);
                    }
                } else {
                    // 在当前表情集滑动
                    if (mOnEmotionPageChangedListener != null) {
                        mOnEmotionPageChangedListener.playBy(mCurrentPagePosition - end, position - end, pageSetEntity);
                    }
                    isEmotionSetChanged = false;
                }
                if (isEmotionSetChanged && mOnEmotionPageChangedListener != null) {
                    mOnEmotionPageChangedListener.emotionSetChanged(pageSetEntity);
                }
                return;
            }
            end += size;
        }
    }

    public void setCurrentPageSet(PageSetEntity pageSetEntity) {
        if (mPageSetAdapter == null || mPageSetAdapter.getCount() <= 0) {
            return;
        }
        setCurrentItem(mPageSetAdapter.getPageSetStartPosition(pageSetEntity));
    }

    public void setOnEmotionPageChangedListener(OnEmotionPageChangedListener onEmotionPageChangedListener) {
        mOnEmotionPageChangedListener = onEmotionPageChangedListener;
    }

    /**
     * 表情页变换回调
     */
    public interface OnEmotionPageChangedListener {

        /**
         * 表情集已改变，回调
         *
         * @param pageSetEntity
         */
        void emotionSetChanged(PageSetEntity pageSetEntity);

        /**
         * 改变表情集到指定位置，回调
         *
         * @param position 相对于当前表情集的位置
         */
        void playTo(int position, PageSetEntity pageSetEntity);

        /**
         * 当前表情集页面改变，回调
         *
         * @param lastPosition 相对于当前表情集的始点位置
         * @param newPosition  相对于当前表情集的终点位置
         */
        void playBy(int lastPosition, int newPosition, PageSetEntity pageSetEntity);
    }

}
