package com.example.library.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.example.library.R;
import com.example.library.utils.LogUtils;
import com.example.library.widget.WaitPorgressDialog;

import java.util.Objects;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午5:33
 * desc   : activity抽象基类
 * version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected WaitPorgressDialog mWaitPorgressDialog;
    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected boolean isTransAnim;
    protected String mClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mClassName = getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        initContentViewBefore();
        setContentView(getLayoutId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initData();
        initView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication application = (BaseApplication) getApplication();
//        Tracker tracker = application.getDefaultTracker();
//        tracker.setScreenName(mClassName);
//        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
            mWaitPorgressDialog = null;
        }
    }

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initContentViewBefore() {

    }

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        mWaitPorgressDialog = new WaitPorgressDialog(this);
        isTransAnim = true;
    }

    /**
     * 初始化view
     * <p>
     * 子类实现 控件绑定、视图初始化等内容
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取当前layouty的布局ID,用于设置当前布局
     * <p>
     * 交由子类实现
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 显示提示框
     *
     * @param msg 提示框内容字符串
     */
    protected void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    /**
     * 隐藏提示框
     */
    protected void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }

    @Override
    public void finish() {
        try {
            hiddenKeyboard();
        } catch (Exception e) {
            LogUtils.dTag(TAG, "Exception==" + e.getMessage());
        }
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 隐藏键盘
     *
     * @return 隐藏键盘结果
     * <p>
     * true:隐藏成功
     * <p>
     * false:隐藏失败
     */
    protected boolean hiddenKeyboard() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this
                .getCurrentFocus()).getWindowToken(), 0);
    }

    protected void hiddenKeyboard(View view) {
        InputMethodManager mInputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (mInputMethodManager != null) {
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void initTitleBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void initTitleBar(Toolbar toolbar) {
        initTitleBar(toolbar, "");
    }

}
