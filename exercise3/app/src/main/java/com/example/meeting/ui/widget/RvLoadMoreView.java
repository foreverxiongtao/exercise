package com.example.meeting.ui.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.example.meeting.R;
/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/27 上午12:07
 *    desc   : loading more view
 *    version: 1.0
 */

public class RvLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.item_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
