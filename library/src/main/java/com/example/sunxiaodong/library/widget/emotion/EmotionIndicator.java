package com.example.sunxiaodong.library.widget.emotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sunxiaodong.library.R;
import com.example.sunxiaodong.library.bean.PageSetEntity;
import com.example.sunxiaodong.library.utils.EmotionKeyboardUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

/**
 * 表情集页面指示器
 * Created by sunxiaodong on 16/4/13.
 */
public class EmotionIndicator extends LinearLayout {

    private static final int MARGIN_LEFT = 4;//指示器间隔 单位dp

    protected Context mContext;
    protected ArrayList<ImageView> mImageViews;//指示器视图
    protected Bitmap mBmpSelect;//指示器选中状态 使用 图片
    protected Bitmap mBmpNormal;//指示器正常状态 使用 图片
    protected AnimatorSet mPlayToAnimatorSet;
    protected AnimatorSet mPlayByInAnimatorSet;
    protected AnimatorSet mPlayByOutAnimatorSet;
    protected LayoutParams mLeftLayoutParams;

    public EmotionIndicator(Context context) {
        super(context);
        init(context);
    }

    public EmotionIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmotionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.setOrientation(HORIZONTAL);

        mBmpSelect = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_point_select);
        mBmpNormal = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_point_normal);

        mLeftLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftLayoutParams.leftMargin = EmotionKeyboardUtils.dip2px(context, MARGIN_LEFT);
    }

    /**
     * 切换表情集
     *
     * @param position
     * @param pageSetEntity
     */
    public void playTo(int position, PageSetEntity pageSetEntity) {
        if (!isShowIndicator(pageSetEntity)) {
            return;
        }
        updateIndicatorCount(pageSetEntity.getPageCount());
        for (ImageView iv : mImageViews) {
            iv.setImageBitmap(mBmpNormal);
        }
        mImageViews.get(position).setImageBitmap(mBmpSelect);
        final ImageView imageViewStrat = mImageViews.get(position);
        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 0.25f, 1.0f);
        if (mPlayToAnimatorSet != null && mPlayToAnimatorSet.isRunning()) {
            mPlayToAnimatorSet.cancel();
            mPlayToAnimatorSet = null;
        }
        mPlayToAnimatorSet = new AnimatorSet();
        mPlayToAnimatorSet.play(animIn1).with(animIn2);
        mPlayToAnimatorSet.setDuration(100);
        mPlayToAnimatorSet.start();
    }

    /**
     * 当前表情集切换界面
     *
     * @param startPosition
     * @param nextPosition
     * @param pageSetEntity
     */
    public void playBy(int startPosition, int nextPosition, PageSetEntity pageSetEntity) {
        if (!isShowIndicator(pageSetEntity)) {
            return;
        }
        updateIndicatorCount(pageSetEntity.getPageCount());
        boolean isShowInAnimOnly = false;
        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }
        if (startPosition < 0) {
            isShowInAnimOnly = true;
            startPosition = nextPosition = 0;
        }
        final ImageView imageViewStrat = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f, 0.25f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f, 0.25f);

        if (mPlayByOutAnimatorSet != null && mPlayByOutAnimatorSet.isRunning()) {
            mPlayByOutAnimatorSet.cancel();
            mPlayByOutAnimatorSet = null;
        }
        mPlayByOutAnimatorSet = new AnimatorSet();
        mPlayByOutAnimatorSet.play(anim1).with(anim2);
        mPlayByOutAnimatorSet.setDuration(100);

        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewNext, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewNext, "scaleY", 0.25f, 1.0f);

        if (mPlayByInAnimatorSet != null && mPlayByInAnimatorSet.isRunning()) {
            mPlayByInAnimatorSet.cancel();
            mPlayByInAnimatorSet = null;
        }
        mPlayByInAnimatorSet = new AnimatorSet();
        mPlayByInAnimatorSet.play(animIn1).with(animIn2);
        mPlayByInAnimatorSet.setDuration(100);

        if (isShowInAnimOnly) {
            mPlayByInAnimatorSet.start();
            return;
        }

        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewStrat.setImageBitmap(mBmpNormal);
                ObjectAnimator animFil1l = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f);
                ObjectAnimator animFill2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f);
                AnimatorSet mFillAnimatorSet = new AnimatorSet();
                mFillAnimatorSet.play(animFil1l).with(animFill2);
                mFillAnimatorSet.start();
                imageViewNext.setImageBitmap(mBmpSelect);
                mPlayByInAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mPlayByOutAnimatorSet.start();
    }

    private boolean isShowIndicator(PageSetEntity pageSetEntity) {
        if (pageSetEntity != null && pageSetEntity.isShowIndicator()) {
            setVisibility(VISIBLE);
            return true;
        } else {
            setVisibility(GONE);
            return false;
        }
    }

    /**
     * 更新指示器数量
     *
     * @param count 当前指示器数量
     */
    private void updateIndicatorCount(int count) {
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        }
        if (count > mImageViews.size()) {
            for (int i = mImageViews.size(); i < count; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageBitmap(i == 0 ? mBmpSelect : mBmpNormal);
                this.addView(imageView, mLeftLayoutParams);
                mImageViews.add(imageView);
            }
        }
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
            } else {
                mImageViews.get(i).setVisibility(VISIBLE);
            }
        }
    }

}
