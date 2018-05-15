package com.huawei.mops.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by tWX366549 on 2016/10/29.
 */
public class UUIDUtile {

    public static String getDeviceId(Context context) throws Exception {
        SharedPreferences sp = context.getSharedPreferences("voosmp_data", Context.MODE_PRIVATE);
        String deviceid = sp.getString("review_policy", null);
        if (deviceid == null) {
            ContentResolver cr = context.getContentResolver();
            deviceid = Settings.Secure.getString(cr, "android_id");
            if (!deviceid.equals("")) {
                deviceid = UUID.nameUUIDFromBytes(deviceid.getBytes("utf8")).toString();
            } else {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceid = tm.getDeviceId();
                if (deviceid != null) {
                    deviceid = UUID.nameUUIDFromBytes(deviceid.getBytes("utf8")).toString();
                } else {
                    deviceid = UUID.randomUUID().toString();
                }
            }
            sp.edit().putString("review_policy", deviceid).apply();
        } else {
            deviceid = UUID.fromString(deviceid).toString();
        }
        return deviceid;
    }
}
