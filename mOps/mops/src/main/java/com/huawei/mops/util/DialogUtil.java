package com.huawei.mops.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huawei.mops.weight.CustomDialog;

/**
 * Created by tWX366549 on 2016/10/29.
 */
public class DialogUtil {

    private static MaterialDialog mMaterialDialog;

    public static void showDialog(Activity activity, int layoutId) {
        if (mMaterialDialog != null) mMaterialDialog = null;
        mMaterialDialog = new MaterialDialog.Builder(activity).customView(layoutId).build();
        if (!mMaterialDialog.isShowing()) {
            mMaterialDialog.show();
        }
    }

    public static void hideDialog() {
        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
            mMaterialDialog.hide();
        }
    }
    public static CustomDialog createDialog(Context context, String msg, int title, int negativeButtonText, int positiveButtonText, boolean isCanCancel, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton(positiveButtonText, positive)
                .setNegativeButton(negativeButtonText, negative);
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(isCanCancel);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        return dialog;
    }
}
