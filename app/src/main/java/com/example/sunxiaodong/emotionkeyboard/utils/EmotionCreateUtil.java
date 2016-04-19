package com.example.sunxiaodong.emotionkeyboard.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunxiaodong.emotionkeyboard.Constants;
import com.example.sunxiaodong.emotionkeyboard.R;
import com.example.sunxiaodong.emotionkeyboard.filter.EmojiFilter;
import com.example.sunxiaodong.library.adapter.EmotionAdapter;
import com.example.sunxiaodong.library.adapter.PageSetAdapter;
import com.example.sunxiaodong.library.bean.EmotionPageEntity;
import com.example.sunxiaodong.library.bean.EmotionPageSetEntity;
import com.example.sunxiaodong.library.image.ImageBase;
import com.example.sunxiaodong.library.interfaces.EmotionClickListener;
import com.example.sunxiaodong.library.interfaces.EmotionDisplayListener;
import com.example.sunxiaodong.library.interfaces.PageViewInstantiateListener;
import com.example.sunxiaodong.library.widget.EmotionEditText;
import com.example.sunxiaodong.library.widget.EmotionPageView;
import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sunxiaodong on 16/4/15.
 */
public class EmotionCreateUtil {

    public static PageSetAdapter sCommonPageSetAdapter;

    public static PageSetAdapter getCommonAdapter(Context context, EmotionClickListener emotionClickListener) {
        if (sCommonPageSetAdapter != null) {
            return sCommonPageSetAdapter;
        }
        PageSetAdapter pageSetAdapter = new PageSetAdapter();

        addEmojiPageSetEntity(pageSetAdapter, emotionClickListener);

        return pageSetAdapter;
    }

    /**
     * 插入emoji表情集
     *
     * @param pageSetAdapter
     * @param emotionClickListener
     */
    public static void addEmojiPageSetEntity(PageSetAdapter pageSetAdapter, final EmotionClickListener emotionClickListener) {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        EmotionPageSetEntity emojiPageSetEntity
                = new EmotionPageSetEntity.Builder()
                .setRowNum(3)
                .setColumnNum(7)
                .setEmotionList(emojiArray)
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(new EmotionDisplayListener<Object>() {
                    @Override
                    public void onBindView(int position, ViewGroup parent, EmotionAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                        final EmojiBean emojiBean = (EmojiBean) object;
                        if (emojiBean == null && !isDelBtn) {
                            return;
                        }
                        viewHolder.emotionItemLayout.setBackgroundResource(R.drawable.emotion_item_bg);
                        if (isDelBtn) {
                            viewHolder.emotionItemIcon.setImageResource(R.mipmap.emotion_del_icon);
                        } else {
                            viewHolder.emotionItemIcon.setImageResource(emojiBean.icon);
                        }
                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (emotionClickListener != null) {
                                    emotionClickListener.onEmotionClick(emojiBean, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                                }
                            }
                        });
                    }
                }))
                .setDelBtnStatus(EmotionPageEntity.DelBtnStatus.LAST)
                .setEmotionIconUri(ImageBase.Scheme.DRAWABLE.toUri("icon_emoji"))
                .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }

    public static PageViewInstantiateListener<EmotionPageEntity> getDefaultEmoticonPageViewInstantiateItem(final EmotionDisplayListener<Object> emoticonDisplayListener) {
        return getEmoticonPageViewInstantiateItem(EmotionAdapter.class, null, emoticonDisplayListener);
    }

    public static PageViewInstantiateListener<EmotionPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, EmotionDisplayListener onEmoticonClickListener) {
        return getEmoticonPageViewInstantiateItem(_class, onEmoticonClickListener, null);
    }

    public static PageViewInstantiateListener<EmotionPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, final EmotionDisplayListener onEmoticonClickListener, final EmotionDisplayListener<Object> emoticonDisplayListener) {
        return new PageViewInstantiateListener<EmotionPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, EmotionPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmotionPageView pageView = new EmotionPageView(container.getContext());
                    pageView.setNumColumns(pageEntity.getColumnNum());
                    pageEntity.setRootView(pageView);
                    try {
                        EmotionAdapter adapter = (EmotionAdapter) newInstance(_class, container.getContext(), pageEntity, onEmoticonClickListener);
                        if (emoticonDisplayListener != null) {
                            adapter.setEmotionDisplayListener(emoticonDisplayListener);
                        }
                        pageView.getEmotionGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, Object... args) throws Exception {
        return newInstance(_Class, 0, args);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, int constructorIndex, Object... args) throws Exception {
        Constructor cons = _Class.getConstructors()[constructorIndex];
        return cons.newInstance(args);
    }

    public static void initEmotionEditText(EmotionEditText emotionEditText) {
        emotionEditText.addEmotionFilter(new EmojiFilter());
//        emotionEditText.addEmotionFilter(new XhsFilter());
    }

}
