package com.example.sunxiaodong.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.sunxiaodong.library.utils.EmotionKeyboardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 和软键盘重叠的功能布局
 * Created by sunxiaodong on 16/4/14.
 */
public class FunctionLayout extends LinearLayout {

    private static final String TAG = FunctionLayout.class.getSimpleName();
    private static final String SXD = "sxd";

    public static final int FUNC_KEY_SOFT_KEYBOARD = Integer.MIN_VALUE;//默认功能key
    public static final int FUNC_KEY_EMOTION = -1;//表情功能key
    public static final int FUNC_KEY_APP = -2;//软件其他功能key

    private final SparseArray<View> mFuncViewArrayMap = new SparseArray<>();//存储着功能Key关联的功能布局，表情和自定义功能等

    private int mCurrentFuncKey = FUNC_KEY_SOFT_KEYBOARD;//当前功能key
    protected int mHeight = 0;//当前功能布局高度

    private OnFuncChangeListener mOnFuncChangeListener;
    private List<OnFuncKeyBoardListener> mOnFuncKeyBoardListenerList;

    public FunctionLayout(Context context) {
        super(context);
        init();
    }

    public FunctionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FunctionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    /**
     * 添加功能布局
     *
     * @param key  代表功能的key
     * @param view 功能视图
     */
    public void addFuncView(int key, View view) {
        if (mFuncViewArrayMap.get(key) != null) {
            return;
        }
        mFuncViewArrayMap.put(key, view);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, params);
        view.setVisibility(GONE);
    }

    /**
     * 隐藏所有的功能视图
     */
    public void hideAllFuncView() {
        for (int i = 0; i < mFuncViewArrayMap.size(); i++) {
            int keyTemp = mFuncViewArrayMap.keyAt(i);
            mFuncViewArrayMap.get(keyTemp).setVisibility(GONE);
        }
        mCurrentFuncKey = FUNC_KEY_SOFT_KEYBOARD;
        setVisibility(false);
    }

    /**
     * 切换功能视图
     *
     * @param key               功能key
     * @param isSoftKeyboardPop 软件盘当前是否弹起状态
     * @param editText          编辑框
     */
    public void toggleFuncView(int key, boolean isSoftKeyboardPop, EditText editText) {
        if (getCurrentFuncKey() == key) {
            if (isSoftKeyboardPop) {
                EmotionKeyboardUtils.closeSoftKeyboard(getContext());
            } else {
                EmotionKeyboardUtils.openSoftKeyboard(editText);
            }
        } else {
            if (isSoftKeyboardPop) {
                EmotionKeyboardUtils.closeSoftKeyboard(getContext());
            }
            showFuncView(key);
        }
    }

    public int getCurrentFuncKey() {
        return mCurrentFuncKey;
    }

    private void showFuncView(int key) {
        if (mFuncViewArrayMap.get(key) == null) {
            return;
        }
        for (int i = 0; i < mFuncViewArrayMap.size(); i++) {
            int keyTemp = mFuncViewArrayMap.keyAt(i);
            if (keyTemp == key) {
                mFuncViewArrayMap.get(keyTemp).setVisibility(VISIBLE);
                Log.i(SXD, TAG + "--showFuncView++VISIBLE:" + keyTemp);
            } else {
                mFuncViewArrayMap.get(keyTemp).setVisibility(GONE);
                Log.i(SXD, TAG + "--showFuncView++GONE:" + keyTemp);
            }
        }
        mCurrentFuncKey = key;
        setVisibility(true);
        if (mOnFuncChangeListener != null) {
            mOnFuncChangeListener.onFuncChange(mCurrentFuncKey);
        }
    }

    /**
     * 设置功能布局的显示或隐藏
     *
     * @param show
     */
    public void setVisibility(boolean show) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (show) {
            setVisibility(VISIBLE);
            params.height = mHeight;
            if (mOnFuncKeyBoardListenerList != null) {
                for (OnFuncKeyBoardListener onFuncKeyBoardListener : mOnFuncKeyBoardListenerList) {
                    onFuncKeyBoardListener.OnFuncPop(mHeight);
                }
            }
        } else {
            setVisibility(GONE);
            params.height = 0;
            if (mOnFuncKeyBoardListenerList != null) {
                for (OnFuncKeyBoardListener onFuncKeyBoardListener : mOnFuncKeyBoardListenerList) {
                    onFuncKeyBoardListener.OnFuncClose();
                }
            }
        }
        setLayoutParams(params);
    }

    public boolean isSoftKeyboard() {
        return mCurrentFuncKey == FUNC_KEY_SOFT_KEYBOARD;
    }

    /**
     * 根据功能布局大小动态更新高度
     *
     * @param height
     */
    public void updateHeight(int height) {
        this.mHeight = height;
    }

    public void addOnKeyBoardListener(OnFuncKeyBoardListener onFuncKeyBoardListener) {
        if (mOnFuncKeyBoardListenerList == null) {
            mOnFuncKeyBoardListenerList = new ArrayList<>();
        }
        mOnFuncKeyBoardListenerList.add(onFuncKeyBoardListener);
    }

    /**
     * 功能视图改变监听
     */
    public interface OnFuncKeyBoardListener {
        /**
         * 功能布局弹起
         */
        void OnFuncPop(int height);

        /**
         * 功能布局关闭
         */
        void OnFuncClose();
    }

    public void setOnFuncChangeListener(OnFuncChangeListener onFuncChangeListener) {
        this.mOnFuncChangeListener = onFuncChangeListener;
    }

    /**
     * 功能改变回调
     */
    public interface OnFuncChangeListener {

        void onFuncChange(int key);

    }

}
