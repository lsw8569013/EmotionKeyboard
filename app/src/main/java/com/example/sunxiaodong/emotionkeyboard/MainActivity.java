package com.example.sunxiaodong.emotionkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunxiaodong.emotionkeyboard.utils.EmotionCreateUtil;
import com.example.sunxiaodong.emotionkeyboard.utils.FilePathUtil;
import com.example.sunxiaodong.emotionkeyboard.view.MyEmotionKeyBoard;
import com.example.sunxiaodong.library.interfaces.EmotionClickListener;
import com.example.sunxiaodong.library.utils.Imager;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private MyEmotionKeyBoard mMyEmotionKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Imager.init(getApplicationContext(), FilePathUtil.IMAGE_CACHE);//初始化图片加载器
        initView();
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.text_view);
        mMyEmotionKeyBoard = (MyEmotionKeyBoard) findViewById(R.id.emotion_keyboard);
        mMyEmotionKeyBoard.setAdapter(EmotionCreateUtil.getCommonAdapter(this, new EmotionClickListener() {
            @Override
            public void onEmotionClick(Object o, int actionType, boolean isDelBtn) {

            }
        }));
        mMyEmotionKeyBoard.getEmotionToolView().addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "ADD", Toast.LENGTH_SHORT).show();
            }
        });
        mMyEmotionKeyBoard.getEmotionToolView().addToolItemView(R.mipmap.icon_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "SETTING", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
