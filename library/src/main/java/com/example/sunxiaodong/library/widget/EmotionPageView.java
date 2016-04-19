package com.example.sunxiaodong.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.example.sunxiaodong.library.R;

/**
 * 每一页显示的GridView
 * Created by sunxiaodong on 16/4/15.
 */
public class EmotionPageView extends RelativeLayout {

    private GridView mEmotionGridView;

    public GridView getEmotionGridView() {
        return mEmotionGridView;
    }

    public void setEmotionGridView(GridView mEmotionGridView) {
        this.mEmotionGridView = mEmotionGridView;
    }

    public EmotionPageView(Context context) {
        super(context);
        init(context);
    }

    public EmotionPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmotionPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.emotion_page_item, this);
        mEmotionGridView = (GridView) view.findViewById(R.id.emotion_grid_view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            mEmotionGridView.setMotionEventSplittingEnabled(false);
        }
        mEmotionGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mEmotionGridView.setCacheColorHint(0);
        mEmotionGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mEmotionGridView.setVerticalScrollBarEnabled(false);
    }

    public void setNumColumns(int columns) {
        mEmotionGridView.setNumColumns(columns);
    }

}
