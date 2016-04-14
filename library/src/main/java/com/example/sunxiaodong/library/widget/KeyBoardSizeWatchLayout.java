package com.example.sunxiaodong.library.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 软键盘大小监测布局
 * 监测原理：利用键盘弹出与收起时，整个界面布局大小的变化来计算大小和获取键盘弹出与收起状态
 * Created by sunxiaodong on 16/4/13.
 */
public class KeyBoardSizeWatchLayout extends RelativeLayout {

    private static final String TAG = KeyBoardSizeWatchLayout.class.getSimpleName();
    private static final String SXD = "sxd";

    private boolean mIsSoftKeyboardPop = false;//当前软件盘是否弹出标识
    private int mValidScreenHeight = 0;//有效的视图可显示屏幕高度
    private int mLastHeight = -1;//之前的键盘高度
    private int mNewHeight = -1;//新的键盘高度

    private List<OnResizeListener> mListenerList;//键盘改变注册监听列表

    public KeyBoardSizeWatchLayout(Context context) {
        super(context);
        init(context);
    }

    public KeyBoardSizeWatchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyBoardSizeWatchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {//视图树中全局布局状态或视图的可见状态改变时，回调
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                if (mValidScreenHeight == 0) {
                    //初始布局进入，bottom为到屏幕底端高度
                    mValidScreenHeight = r.bottom;
                    Log.i(SXD, TAG + "--首次布局++mValidScreenHeight：" + mValidScreenHeight);
                }
                Log.i(SXD, TAG + "--布局变化++r.bottom：" + r.bottom);
                mNewHeight = mValidScreenHeight - r.bottom;
                if (mLastHeight != -1 && mNewHeight != mLastHeight) {
                    //不是初始化布局，且前后高度不同时进入，用以向外回调高度变化，处理
                    Log.i(SXD, TAG + "--布局变化++mNewHeight：" + mNewHeight);
                    if (mNewHeight > 0) {
                        mIsSoftKeyboardPop = true;
                        if (mListenerList != null) {
                            for (OnResizeListener l : mListenerList) {
                                l.OnSoftPop(mNewHeight);
                            }
                        }
                    } else {
                        mIsSoftKeyboardPop = false;
                        if (mListenerList != null) {
                            for (OnResizeListener l : mListenerList) {
                                l.OnSoftClose();
                            }
                        }
                    }
                }
                mLastHeight = mNewHeight;
            }
        });
    }

    public boolean isSoftKeyboardPop() {
        return mIsSoftKeyboardPop;
    }

    public void addOnResizeListener(OnResizeListener onResizeListener) {
        if (mListenerList == null) {
            mListenerList = new ArrayList<>();
        }
        mListenerList.add(onResizeListener);
    }

    /**
     * 键盘变化回调
     */
    public interface OnResizeListener {
        /**
         * 软键盘弹起
         */
        void OnSoftPop(int height);

        /**
         * 软键盘关闭
         */
        void OnSoftClose();
    }

}
