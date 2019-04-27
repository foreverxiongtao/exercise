package com.example.library.base.fragment;

import com.example.library.base.BasePresenter;
import com.example.library.base.IBaseFragment;
import com.example.library.base.IBaseModel;

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/27 上午12:50
 *    desc   : mvp模式下fragment
 *    version: 1.0
 */

public abstract class BaseMVPCompatFragment<P extends BasePresenter, M extends IBaseModel> extends
        BaseCompatFragment implements IBaseFragment {

    public P mPresenter;
    public M mIMode;

    /**
     * 在监听器之前把数据准备好
     */
    public void initData() {
        super.initData();

        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mIMode = (M) mPresenter.getModel();
            if (mIMode != null) {
                mPresenter.attachMV(mIMode, this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachMV();
        }
    }

    @Override
    public void showWaitDialog(String msg) {
        showProgressDialog(msg);
    }

    @Override
    public void hideWaitDialog() {
        hideProgressDialog();
    }
}
