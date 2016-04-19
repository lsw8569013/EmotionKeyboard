package com.example.sunxiaodong.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sunxiaodong.library.R;
import com.example.sunxiaodong.library.bean.EmotionPageEntity;
import com.example.sunxiaodong.library.interfaces.EmotionClickListener;
import com.example.sunxiaodong.library.interfaces.EmotionDisplayListener;

import java.util.ArrayList;

/**
 * 表情网格显示适配器
 * Created by sunxiaodong on 16/4/15.
 */
public class EmotionAdapter<T> extends BaseAdapter {

    protected final int DEF_HEIGHTMAXTATIO = 2;
    protected final int mDefalutItemHeight;

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected ArrayList<T> mData = new ArrayList<>();
    protected EmotionPageEntity mEmotionPageEntity;//每页表情数据实体

    protected double mItemHeightMaxRatio;
    protected int mItemHeightMax;
    protected int mItemHeightMin;
    protected int mItemHeight;
    protected int mDelbtnPosition;

    protected EmotionDisplayListener mEmotionDisplayListener;
    protected EmotionClickListener mEmotionClickListener;//表情项点击回调

    public EmotionAdapter(Context context, EmotionPageEntity emotionPageEntity, EmotionClickListener emotionClickListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mEmotionPageEntity = emotionPageEntity;
        this.mEmotionClickListener = emotionClickListener;
        this.mItemHeightMaxRatio = DEF_HEIGHTMAXTATIO;
        this.mDelbtnPosition = -1;
        this.mDefalutItemHeight = this.mItemHeight = (int) context.getResources().getDimension(R.dimen.dimen_1080p_96);
        this.mData.addAll(emotionPageEntity.getEmotionList());
        checkDelBtn(emotionPageEntity);
    }

    private void checkDelBtn(EmotionPageEntity entity) {
        EmotionPageEntity.DelBtnStatus delBtnStatus = entity.getDelBtnStatus();
        if (EmotionPageEntity.DelBtnStatus.GONE.equals(delBtnStatus)) {
            return;
        }
        if (EmotionPageEntity.DelBtnStatus.FOLLOW.equals(delBtnStatus)) {
            mDelbtnPosition = getCount();
            mData.add(null);
        } else if (EmotionPageEntity.DelBtnStatus.LAST.equals(delBtnStatus)) {
            int max = entity.getColumnNum() * entity.getRowNum();
            while (getCount() < max) {
                mData.add(null);
            }
            mDelbtnPosition = getCount() - 1;
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.emotion_grid_view_item_layout, null);
            viewHolder.rootView = convertView;
            viewHolder.emotionItemLayout = (LinearLayout) convertView.findViewById(R.id.emoticon_item_layout);
            viewHolder.emotionItemIcon = (ImageView) convertView.findViewById(R.id.emoticon_item_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindView(position, parent, viewHolder);
        updateUI(viewHolder, parent);
        return convertView;
    }

    protected void bindView(int position, ViewGroup parent, ViewHolder viewHolder) {
        if (mEmotionDisplayListener != null) {
            mEmotionDisplayListener.onBindView(position, parent, viewHolder, mData.get(position), position == mDelbtnPosition);
        }
    }

    protected boolean isDelBtn(int position) {
        return position == mDelbtnPosition;
    }

    protected void updateUI(ViewHolder viewHolder, ViewGroup parent) {
        if (mDefalutItemHeight != mItemHeight) {
            viewHolder.emotionItemIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mItemHeight));
        }
        mItemHeightMax = this.mItemHeightMax != 0 ? this.mItemHeightMax : (int) (mItemHeight * mItemHeightMaxRatio);
        mItemHeightMin = this.mItemHeightMin != 0 ? this.mItemHeightMin : mItemHeight;
        int realItemHeight = ((View) parent.getParent()).getMeasuredHeight() / mEmotionPageEntity.getRowNum();
        realItemHeight = Math.min(realItemHeight, mItemHeightMax);
        realItemHeight = Math.max(realItemHeight, mItemHeightMin);
        viewHolder.emotionItemLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, realItemHeight));
    }

    public void setEmotionDisplayListener(EmotionDisplayListener emotionDisplayListener) {
        this.mEmotionDisplayListener = emotionDisplayListener;
    }

    public void setItemHeightMaxRatio(double mItemHeightMaxRatio) {
        this.mItemHeightMaxRatio = mItemHeightMaxRatio;
    }

    public void setItemHeightMax(int mItemHeightMax) {
        this.mItemHeightMax = mItemHeightMax;
    }

    public void setItemHeightMin(int mItemHeightMin) {
        this.mItemHeightMin = mItemHeightMin;
    }

    public void setItemHeight(int mItemHeight) {
        this.mItemHeight = mItemHeight;
    }

    public void setDelbtnPosition(int mDelbtnPosition) {
        this.mDelbtnPosition = mDelbtnPosition;
    }

    public static class ViewHolder {
        public View rootView;
        public LinearLayout emotionItemLayout;
        public ImageView emotionItemIcon;
    }

}
