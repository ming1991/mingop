package com.huawei.mops;

import android.app.Application;

import com.huawei.mops.db.DaoMaster;
import com.huawei.mops.db.DaoSession;
import com.huawei.mops.util.CatchHandler;
import com.huawei.mops.util.DeviceUtil;
import com.huawei.mops.util.ToastUtil;

import org.greenrobot.greendao.database.Database;

/**
 * Created by tWX366549 on 2016/10/18.
 */
public class MopsApp extends Application {

    public static String deviceId;
    public static MopsApp instance;

    public static boolean isCN = true;

    public static String language = "en_CN";
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ToastUtil.register(this);
        isCN = getResources().getConfiguration().locale.getLanguage().contains("zh");
        language = isCN ? "en_CN" : "en_US";
        if (BuildConfig.LOG_DEBUG) {
            deviceId = "a00000556e4bcf";
        } else {
            deviceId = DeviceUtil.getDeviceId(getApplicationContext());
        }
//        CatchHandler.getInstance().init(getApplicationContext());
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mops.db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);              // 初始化 JPush
//        JPushInterface.setLatestNotificationNumber(getApplicationContext(), 1);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
