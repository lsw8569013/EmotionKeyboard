package com.example.sunxiaodong.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.sunxiaodong.library.adapter.PageSetAdapter;
import com.example.sunxiaodong.library.bean.PageSetEntity;
import com.example.sunxiaodong.library.utils.EmotionKeyboardUtils;
import com.example.sunxiaodong.library.widget.DynamicHeightLayout;
import com.example.sunxiaodong.library.widget.EmotionEditText;
import com.example.sunxiaodong.library.widget.FunctionLayout;
import com.example.sunxiaodong.library.widget.emotion.EmotionDisplayView;
import com.example.sunxiaodong.library.widget.emotion.EmotionIndicator;
import com.example.sunxiaodong.library.widget.emotion.EmotionToolView;

import java.util.ArrayList;

/**
 * 表情键盘
 * Created by sunxiaodong on 16/4/13.
 */
public class EmotionKeyBoard extends DynamicHeightLayout implements EmotionDisplayView.OnEmotionPageChangedListener, EmotionToolView.OnToolItemClickListener, FunctionLayout.OnFuncChangeListener, EmotionEditText.OnBackKeyClickListener {

    private static final String TAG = EmotionKeyBoard.class.getSimpleName();
    private static final String SXD = "sxd";

    protected LayoutInflater mInflater;

    protected FunctionLayout mFunctionLayout;
    protected EmotionDisplayView mEmotionDisplayView;
    protected EmotionIndicator mEmotionIndicator;
    protected EmotionToolView mEmotionToolView;

    protected boolean mDispatchKeyEventPreImeLock = false;

    public EmotionKeyBoard(Context context) {
        super(context);
        init(context);
    }

    public EmotionKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmotionKeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initOperaLayout();
        initFunctionView();
    }

    protected View initEmotion() {
        View emotionLayout = mInflater.inflate(R.layout.emotion_layout, null);
        mEmotionDisplayView = (EmotionDisplayView) emotionLayout.findViewById(R.id.emotion_display_view);
        mEmotionIndicator = (EmotionIndicator) emotionLayout.findViewById(R.id.emotion_indicator);
        mEmotionToolView = (EmotionToolView) emotionLayout.findViewById(R.id.emotion_tool_view);

        mEmotionDisplayView.setOnEmotionPageChangedListener(this);
        mEmotionToolView.setOnToolItemClickListener(this);
        return emotionLayout;
    }

    protected void initOperaLayout() {
        mFunctionLayout = (FunctionLayout) findViewById(R.id.id_function_layout);
    }

    private void initFunctionView() {
        View emotionLayout = initEmotion();
        mFunctionLayout.addFuncView(FunctionLayout.FUNC_KEY_EMOTION, emotionLayout);
        mFunctionLayout.setOnFuncChangeListener(this);
    }

    protected void reSet() {
        EmotionKeyboardUtils.closeSoftKeyboard(getContext());
        mFunctionLayout.hideAllFuncView();
    }

    /**
     * 向表情键盘UI设置数据
     * @param pageSetAdapter
     */
    public void setAdapter(PageSetAdapter pageSetAdapter) {
        if (pageSetAdapter != null) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null) {
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    //设置底部表情操作栏数据
                    mEmotionToolView.addToolItemView(pageSetEntity);
                    Log.i(SXD, TAG + "--setAdapter++pageSetEntity:" + pageSetEntity.getEmotionSetId());
                }
            }
        }
        mEmotionDisplayView.setAdapter(pageSetAdapter);//设置表情页数据
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        mFunctionLayout.updateHeight(height);
        Log.i(SXD, TAG + "--onSoftKeyboardHeightChanged++height:" + height);
    }

    @Override
    public void emotionSetChanged(PageSetEntity pageSetEntity) {
        mEmotionToolView.selectToolItem(pageSetEntity.getEmotionSetId());
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmotionIndicator.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int lastPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmotionIndicator.playBy(lastPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onItemClick(PageSetEntity pageSetEntity) {
        mEmotionDisplayView.setCurrentPageSet(pageSetEntity);
    }

    protected void toggleFuncView(int key, EditText editText) {
        mFunctionLayout.toggleFuncView(key, isSoftKeyboardPop(), editText);
    }

    @Override
    public void onFuncChange(int key) {

    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        mFunctionLayout.setVisibility(true);
        onFuncChange(FunctionLayout.FUNC_KEY_SOFT_KEYBOARD);//键盘弹起时，默认显示键盘
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (mFunctionLayout.isSoftKeyboard()) {
            reSet();
        } else {
            onFuncChange(mFunctionLayout.getCurrentFuncKey());
        }
    }

    @Override
    public void onBackKeyClick() {
        if (mFunctionLayout.isShown()) {
            mDispatchKeyEventPreImeLock = true;
            reSet();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (mFunctionLayout.isShown()) {
                    reSet();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    public EmotionToolView getEmotionToolView() {
        return mEmotionToolView;
    }

}
