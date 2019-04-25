package com.example.library.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import com.example.library.R;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午5:50
 * desc   : loading框
 * version: 1.0
 */

public class WaitPorgressDialog extends ProgressDialog {


    public WaitPorgressDialog(Context context) {
        this(context, 0);
    }

    public WaitPorgressDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setIndeterminateDrawable(getContext().getDrawable(R.drawable.bg_load));
        } else {
            setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.bg_load));
        }
    }
}
