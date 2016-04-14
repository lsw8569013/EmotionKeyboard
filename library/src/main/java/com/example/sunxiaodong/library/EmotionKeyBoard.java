package com.example.sunxiaodong.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sunxiaodong.library.widget.DynamicHeightLayout;
import com.example.sunxiaodong.library.widget.emotion.EmotionDisplayView;
import com.example.sunxiaodong.library.widget.emotion.EmotionIndicator;
import com.example.sunxiaodong.library.widget.emotion.EmotionToolView;

/**
 * Created by sunxiaodong on 16/4/13.
 */
public class EmotionKeyBoard extends DynamicHeightLayout {

    protected LayoutInflater mInflater;

    protected EmotionDisplayView mEmotionDisplayView;
    protected EmotionIndicator mEmotionIndicator;
    protected EmotionToolView mEmotionToolView;

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

    }

    protected void initEmotion(){
        View emotionLayout = mInflater.inflate(R.layout.emotion_layout, null);
        mEmotionDisplayView = (EmotionDisplayView) emotionLayout.findViewById(R.id.emotion_display_view);
        mEmotionIndicator = (EmotionIndicator) emotionLayout.findViewById(R.id.emotion_indicator);
        mEmotionToolView = (EmotionToolView) emotionLayout.findViewById(R.id.emotion_tool_view);


    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {

    }

}
