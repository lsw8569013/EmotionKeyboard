package com.example.sunxiaodong.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunxiaodong.library.R;
import com.example.sunxiaodong.library.utils.EmotionKeyboardUtils;

/**
 * 动态高度布局
 * Created by sunxiaodong on 16/4/13.
 */
public abstract class DynamicHeightLayout extends KeyBoardSizeWatchLayout implements KeyBoardSizeWatchLayout.OnResizeListener {

    private static final String TAG = DynamicHeightLayout.class.getSimpleName();
    private static final String SXD = "sxd";

    private static final int ID_CHILD = R.id.id_dynamiclayout;

    private Context mContext;

    protected int mSoftKeyboardHeight;//键盘高度
    protected int mMaxParentHeight;

    public DynamicHeightLayout(Context context) {
        super(context);
        init(context);
    }

    public DynamicHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicHeightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mSoftKeyboardHeight = EmotionKeyboardUtils.getDefKeyboardHeight(mContext);
        Log.i(SXD, TAG + "--init++mSoftKeyboardHeight:" + mSoftKeyboardHeight);
        addOnResizeListener(this);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childNum = getChildCount();
        if (childNum > 1) {
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child,index, params);
        if (childNum == 0) {
            if (child.getId() < 0) {
                child.setId(ID_CHILD);
            }
            LayoutParams paramsChild = (LayoutParams) child.getLayoutParams();
            paramsChild.addRule(ALIGN_PARENT_BOTTOM);//加入的第一个布局，定位在最下方
            child.setLayoutParams(paramsChild);
        } else if (childNum == 1) {
            LayoutParams paramsChild = (LayoutParams) child.getLayoutParams();
            paramsChild.addRule(ABOVE, ID_CHILD);//加入的第二个布局，定位在第一个布局的上方
            child.setLayoutParams(paramsChild);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(SXD, TAG + "--onFinishInflate++mSoftKeyboardHeight:" + mSoftKeyboardHeight);
        onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {//布局尺寸改变时，回调
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
        Log.i(SXD, TAG + "--onSizeChanged++mMaxParentHeight:" + mMaxParentHeight);
    }

    @Override
    public void OnSoftPop(int height) {//键盘弹出时，回调
        Log.i(SXD, TAG + "--OnSoftPop++height:" + height);
        Log.i(SXD, TAG + "--OnSoftPop++mSoftKeyboardHeight:" + mSoftKeyboardHeight);
        if (mSoftKeyboardHeight != height) {
            mSoftKeyboardHeight = height;
            EmotionKeyboardUtils.setDefKeyboardHeight(mContext, mSoftKeyboardHeight);
            onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
        }
    }

    @Override
    public void OnSoftClose() {

    }

    public abstract void onSoftKeyboardHeightChanged(int height);

}
