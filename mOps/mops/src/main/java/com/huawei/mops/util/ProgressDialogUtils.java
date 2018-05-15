package com.huawei.mops.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by tWX366549 on 2016/11/29.
 */
public class ProgressDialogUtils {


    private static ProgressDialog dialog;

    public static void show(Activity activity, String msg) {
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.show();
    }


    public static void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
