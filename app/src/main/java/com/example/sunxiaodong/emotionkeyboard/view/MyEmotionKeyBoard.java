package com.example.sunxiaodong.emotionkeyboard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunxiaodong.emotionkeyboard.R;
import com.example.sunxiaodong.emotionkeyboard.utils.EmotionCreateUtil;
import com.example.sunxiaodong.library.EmotionKeyBoard;
import com.example.sunxiaodong.library.widget.EmotionEditText;
import com.example.sunxiaodong.library.widget.FunctionLayout;

/**
 * Created by sunxiaodong on 16/4/14.
 */
public class MyEmotionKeyBoard extends EmotionKeyBoard implements View.OnClickListener, EmotionEditText.OnBackKeyClickListener {

    private EmotionEditText mEditText;
    private ImageView mImageBtn;
    private ImageView mEmotionBtn;
    private TextView mSendBtn;

    public MyEmotionKeyBoard(Context context) {
        super(context);
    }

    public MyEmotionKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEmotionKeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initOperaLayout() {
        View operaLayout = mInflater.inflate(R.layout.my_emotion_keyboard_layout, this);
        mEditText = (EmotionEditText) operaLayout.findViewById(R.id.edit_text);
        mEditText.setOnBackKeyClickListener(this);
        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mEditText.isFocused()) {
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
        mEditText.setOnClickListener(this);
        EmotionCreateUtil.initEmotionEditText(mEditText);
        mImageBtn = (ImageView) operaLayout.findViewById(R.id.image_btn);
        mEmotionBtn = (ImageView) operaLayout.findViewById(R.id.emotion_btn);
        mSendBtn = (TextView) operaLayout.findViewById(R.id.send_btn);
        mImageBtn.setOnClickListener(this);
        mEmotionBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        super.initOperaLayout();
    }

    @Override
    protected void reSet() {
        super.reSet();
        mEmotionBtn.setImageResource(R.mipmap.ic_emotion);
    }

    @Override
    public void onFuncChange(int key) {
        super.onFuncChange(key);
        switch (key) {
            case FunctionLayout.FUNC_KEY_EMOTION:
                mEmotionBtn.setImageResource(R.mipmap.ic_keyboard);
                break;
            case FunctionLayout.FUNC_KEY_APP:
            default:
                mEmotionBtn.setImageResource(R.mipmap.ic_emotion);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn:

                break;
            case R.id.emotion_btn:
                toggleFuncView(FunctionLayout.FUNC_KEY_EMOTION, mEditText);
                break;
            case R.id.send_btn:

                break;
            case R.id.edit_text:
                toggleFuncView(FunctionLayout.FUNC_KEY_EMOTION, mEditText);
                break;
        }
    }

}
