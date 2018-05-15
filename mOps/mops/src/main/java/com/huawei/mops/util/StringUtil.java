package com.huawei.mops.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.huawei.mops.R;
import com.huawei.mops.bean.ShowCaseStatisticalInfo;

import java.text.DecimalFormat;

/**
 * Created by sll on 2016/3/30.
 */
public class StringUtil {

    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }

    public static void copy(Context mContext, String stripped) {
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("content", stripped);
        clipboard.setPrimaryClip(clip);
        ToastUtil.showToast("复制成功");
    }

    public static SpannableStringBuilder getStyleText(Context context, ShowCaseStatisticalInfo info) {
        int total = info.getTotalShowCase();
        int normal = info.getNormal();
        int exceptionPro = info.getAbnormal();
        int maintain = info.getMaintenance();
        int unavailable = info.getError();
        int unkown = info.getUnKnow();
        String notice = String.format(
                context.getResources().getString(R.string.inspection_statistical_text), total, normal, exceptionPro, maintain, unavailable, unkown);
        int index[] = new int[6];
        index[0] = notice.indexOf(String.valueOf(total));
        index[1] = notice.indexOf(String.valueOf(normal));
        index[2] = notice.indexOf(String.valueOf(exceptionPro));
        index[3] = notice.indexOf(String.valueOf(maintain));
        index[4] = notice.indexOf(String.valueOf(unavailable));
        index[5] = notice.indexOf(String.valueOf(unkown));
        SpannableStringBuilder textStyle = new SpannableStringBuilder(notice);
//        textStyle.setSpan(new ForegroundColorSpan(Color.RED), index[2], index[2] + String.valueOf(exceptionPro).length(),
//                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        textStyle.setSpan(new ForegroundColorSpan(Color.RED), index[4], index[4] +
//                String.valueOf(unavailable).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return textStyle;
    }

    public static SpannableStringBuilder getStyleText(Context context, int stringId, String str) {
        String notice = String.format(context.getResources().getString(stringId), str);
        int index = notice.indexOf(str);
        SpannableStringBuilder textStyle = new SpannableStringBuilder(notice);
        textStyle.setSpan(new ForegroundColorSpan(Color.RED), index, index + str.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return textStyle;
    }

    public static SpannableStringBuilder getStyleText(Context context, int count) {
        String notice = String.format(context.getResources().getString(R.string.alarm_statistical_text), count);
        int index[] = new int[1];
        index[0] = notice.indexOf(String.valueOf(count));
        SpannableStringBuilder textStyle = new SpannableStringBuilder(notice);
        textStyle.setSpan(new ForegroundColorSpan(Color.RED), index[0], index[0] + String.valueOf(count).length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return textStyle;
    }
}
