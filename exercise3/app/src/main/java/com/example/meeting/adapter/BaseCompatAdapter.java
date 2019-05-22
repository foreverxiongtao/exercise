package com.example.meeting.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meeting.ui.widget.RvLoadMoreView;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 上午12:08
 * desc   : base adapter
 * version: 1.0
 */

public abstract class BaseCompatAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public BaseCompatAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        init();
    }

    public BaseCompatAdapter(@Nullable List<T> data) {
        super(data);
        init();
    }

    public BaseCompatAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
        init();
    }

    private void init() {
        setLoadMoreView(new RvLoadMoreView());
        setEnableLoadMore(true);
        openLoadAnimation();//Turn on default animation loading (only enable animation when opening new item)
    }
}
