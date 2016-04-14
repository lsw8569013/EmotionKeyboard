package com.example.sunxiaodong.library.widget.emotion;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sunxiaodong.library.R;
import com.example.sunxiaodong.library.bean.PageSetEntity;
import com.example.sunxiaodong.library.utils.Imager;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * 表情工具视图
 * Created by sunxiaodong on 16/4/13.
 */
public class EmotionToolView extends RelativeLayout {

    private LayoutInflater mInflater;
    private Context mContext;

    private int mItemWidth = 168;

    private HorizontalScrollView mEmotionToolHSV;//用于定位 固定操作项
    private LinearLayout mEmotionToolLL;//用于添加表情项的布局

    private OnToolItemClickListener mOnToolItemClickListener;

    protected ArrayList<View> mEmotionToolItemList = new ArrayList<>();//表情项上的图片视图

    public EmotionToolView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public EmotionToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EmotionToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.EmotionToolView, defStyleAttr, 0);
        mItemWidth = (int) array.getDimension(R.styleable.EmotionToolView_itemWidth, mItemWidth);
        array.recycle();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.emotion_tool_layout, this);
        mEmotionToolHSV = (HorizontalScrollView) findViewById(R.id.emotion_tool_hsv);
        mEmotionToolLL = (LinearLayout) findViewById(R.id.emotion_tool_ll);
    }

    protected View getItemView() {
        return mInflater == null ? null : mInflater.inflate(R.layout.emotion_tool_item_layout, null);
    }

    protected View getItemEmotionIcon(View parentView) {
        return parentView.findViewById(R.id.emotion_icon);
    }

    /**
     * 初始化每一个操作与表情项
     *
     * @param toolItemView    操作项视图
     * @param recId           操作项图片资源id
     * @param pageSetEntity   表情
     * @param onClickListener 点击回调
     */
    private void initToolItemView(View toolItemView, int recId, final PageSetEntity pageSetEntity, OnClickListener onClickListener) {
        ImageView itemEmotionIcon = (ImageView) getItemEmotionIcon(toolItemView);
        if (recId > 0) {
            itemEmotionIcon.setImageResource(recId);
        }
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
        itemEmotionIcon.setLayoutParams(imgParams);
        if (pageSetEntity != null) {
            itemEmotionIcon.setTag(R.id.id_emotion_icon_item, pageSetEntity);
            Imager.getInstance().loadImage(itemEmotionIcon, pageSetEntity.getEmotionIconUri());
        }
        toolItemView.setOnClickListener(onClickListener != null ? onClickListener : new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnToolItemClickListener != null && pageSetEntity != null) {
                    mOnToolItemClickListener.onItemClick(pageSetEntity);
                }
            }
        });
    }

    /**
     * 在底部表情操作栏增加固定的工具项，如增加表情操作项，表情设置操作项
     *
     * @param isRight         是否放置在最右，否则放置在最左
     * @param recId           操作项上显示的图片的资源id
     * @param pageSetEntity   表情集
     * @param onClickListener 点击事件
     */
    public void addFixedToolItemView(boolean isRight, int recId, final PageSetEntity pageSetEntity, OnClickListener onClickListener) {
        View toolItemView = getItemView();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams hsvParams = (LayoutParams) mEmotionToolHSV.getLayoutParams();
        if (toolItemView.getId() <= 0) {
            toolItemView.setId(isRight ? R.id.id_tool_item_right : R.id.id_tool_item_left);
        }
        if (isRight) {
            params.addRule(ALIGN_PARENT_RIGHT);
            hsvParams.addRule(LEFT_OF, toolItemView.getId());
        } else {
            params.addRule(ALIGN_PARENT_LEFT);
            hsvParams.addRule(RIGHT_OF, toolItemView.getId());
        }
        addView(toolItemView, params);
        mEmotionToolHSV.setLayoutParams(hsvParams);
        initToolItemView(toolItemView, recId, pageSetEntity, onClickListener);
    }

    /**
     * 动态增加表情项
     *
     * @param pageSetEntity
     */
    public void addToolItemView(PageSetEntity pageSetEntity) {
        addToolItemView(0, pageSetEntity, null);
    }

    /**
     * 动态增加表情项
     *
     * @param recId
     * @param onClickListener
     */
    public void addToolItemView(int recId, OnClickListener onClickListener) {
        addToolItemView(recId, null, onClickListener);
    }

    /**
     * 动态增加表情项
     *
     * @param recId           表情项图片资源id
     * @param pageSetEntity   表情集
     * @param onClickListener 点击回调
     */
    public void addToolItemView(int recId, final PageSetEntity pageSetEntity, OnClickListener onClickListener) {
        View toolItemView = getItemView();
        initToolItemView(toolItemView, recId, pageSetEntity, onClickListener);
        mEmotionToolLL.addView(toolItemView);
        mEmotionToolItemList.add(getItemEmotionIcon(toolItemView));
    }

    /**
     * 选择 表情项
     *
     * @param emotionSetId 表情集id
     */
    public void selectToolItem(String emotionSetId) {
        if (TextUtils.isEmpty(emotionSetId)) {
            return;
        }
        int position = 0;
        for (int i = 0; i < mEmotionToolItemList.size(); i++) {
            Object object = mEmotionToolItemList.get(i).getTag(R.id.id_emotion_icon_item);
            if (object != null && object instanceof PageSetEntity && emotionSetId.equals(((PageSetEntity) object).getEmotionSetId())) {
                mEmotionToolItemList.get(i).setBackgroundColor(getResources().getColor(R.color.color_d9d9d9));
                position = i;
            } else {
                mEmotionToolItemList.get(i).setBackgroundResource(R.drawable.drawable_ffffff_d9d9d9_selector);
            }
        }
        scrollToToolItemPosition(position);
    }

    /**
     * 滚动到 位置标识 所在位置的表情项
     *
     * @param position 要滚动到的位置
     */
    private void scrollToToolItemPosition(final int position) {
        int childCount = mEmotionToolLL.getChildCount();
        if (position < childCount) {
            mEmotionToolHSV.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = mEmotionToolHSV.getScrollX();

                    int childX = (int) ViewHelper.getX(mEmotionToolLL.getChildAt(position));

                    if (childX < mScrollX) {
                        mEmotionToolHSV.scrollTo(childX, 0);
                        return;
                    }

                    int childWidth = mEmotionToolLL.getChildAt(position).getWidth();
                    int hsvWidth = mEmotionToolHSV.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if (childRight > scrollRight) {
                        mEmotionToolHSV.scrollTo(childRight - scrollRight, 0);
                        return;
                    }
                }
            });
        }
    }

    public void setOnToolItemClickListener(OnToolItemClickListener onToolItemClickListener) {
        this.mOnToolItemClickListener = onToolItemClickListener;
    }

    public interface OnToolItemClickListener {
        void onItemClick(PageSetEntity pageSetEntity);
    }

}
