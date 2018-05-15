package com.huawei.test19;

import android.os.Environment;

import java.io.File;

/**
 * Created by tWX366549 on 2016/7/18.
 */
public class Constant {
    public final static String OBSERVED_PACKAGE = "com.uplus.onphone";
    public final static String ORIGINAL_PATH = "/data/data/com.uplus.onphone/shared_prefs";
    public final static String DEFAULT_FILE_NAME = "default_settings";
    public final static String ORIGINAL_FILE_NAME = "SETTINGS.xml";
    public final static String BACKUP_FILE_NAME = "SETTINGS.backup.xml";
    public final static String LOCAL_PATH = "/mnt/sdcard/account";
    public final static String LOG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "log.txt";

    public final static int STATE_DEFAULT = 0;
    public final static int STATE_APPLY_SUCCESSED = 1;
    public final static int STATE_RELEASE_SUCCESSED = 2;
    public final static int STATE_RELEASE_FAILED = 3;
    /**
     * 释放失败后，周期性释放时间，单位：min
     */
    public final static int PERIOD_TIME = 5;
    public final static String BASE_URL = "http://10.74.204.92:8080/web/";
    public final static String APPLY_URL = "/CSICLab/ignore/m5/useResource/applyAccount.do";
    public final static String RELEASE_URL = "/CSICLab/ignore/m5/useResource/releaseAccount.do";
    public final static String ALL_URL = "/CSICLab/ignore/m5/useResource/queryAllAccountInfo.do";
    public final static String REAPPLY_URL = "/CSICLab/ignore/m5/useResource/addApplyTime.do";

    public final static String APP_VERSION_URL = "/CSICLab/fileOperate/ignore/m5/app/checkAppVersion.do";
    public final static String APK_DOWNLOAD_URL = "/CSICLab/fileOperate/ignore/m5/app/downloadAppFile.do";
    public final static String ACCOUNT_VERSION_URL = "/CSICLab/fileOperate/ignore/m5/useResource/checkAccVersion.do";

    public final static String ACTION_ON_CLOCK = "com.huawei.lguplus.onClock";
    public final static String ACTION_ON_FAILED = "com.huawei.lguplus.onFailed";
    public final static String ACTION_ON_APPLY_SUCCESS = "com.huawei.lguplus.onApplySuccess";
    public final static String ACTION_ON_RELEASE_FAILED = "com.huawei.lguplus.onReleaseFailed";
    public final static String ACTION_ON_RELEASE_SUCCESS = "com.huawei.lguplus.onReleaseSuccess";
    public final static String ACTION_ON_QUERY_SUCCESS = "com.huawei.lguplus.onQuerySuccess";
    public final static String ACTION_ON_CLOCK_COMPLETE = "com.huawei.lguplus.onClockComplete";
    public final static String ACTION_APPLY = "com.huawei.lguplus.APPLY";
    public final static String ACTION_RELEASE = "com.huawei.lguplus.RELEASE";
    public final static String ACTION_QUERY = "com.huawei.lguplus.QUERT";
    public final static String ACTION_CANCEL = "com.huawei.lguplus.CANCEL";
    public final static String ACTION_ON_STARTCOMMOND = "com.huawei.lguplus.onStartCommand";
    public final static String ACTION_CHECK_VERSION = "com.huawei.lguplus.CHECK_VERSION";
    public final static String ACTION_CONNECT_START = "com.huawei.lguplus.CONNECT_START";
    public final static String ACTION_CONNECT_SUCCESS = "com.huawei.lguplus.CONNECT_SUCCESS";
    public final static String ACTION_CONNECT_FAILED = "com.huawei.lguplus.CONNECT_FAILED";
    public final static String ACTION_CONNECT_DESTROYED = "com.huawei.lguplus.CONNECT_DESTROYED";


    public final static int ACTION_APPLY_CODE = 0;
    public final static int ACTION_RELEASE_CODE = 1;
    public final static int ACTION_QUERY_CODE = 2;
    public final static int ACTION_SHOW_TIMER_DIALOG_CODE = 3;
    public final static int ACTION_CANCEL_LOADING_CODE = 4;
    public final static int ACTION_CHECK_VERSION_CODE = 5;
    public final static int ACTION_ON_CLOCK_CODE = 6;
    public final static int ACTION_ON_CLOCK_COMPLIETE_CODE = 7;

    public final static int DEFAULT_EXPIR_TIME = 30;

    public final static int DEFAULT_REPLAY_EXPIR_TIME = 30;

    public final static long DEFAULT_NOTICE_TIME = 300000;

    public final static int CONNECT_TIME_OUT = 3;

    public final static String LGU = "1";
    public final static String ELISA = "2";

    public static class Modle {
        public static final String PAD = "PAD";
        public static final String CSIC = "CSIC";
    }

    public static class EventCode {

        public static final int HEART_BEAT = 1;
        public static final int FIRST_CONNETCT = 3;

        public static final int APPLY_CODE = 5;
        public static final int APPLY_RESPONSE_CODE = 5;
        public static final int RELEASE_CODE = 6;
        public static final int RELEASE_RESPONSE_CODE = 6;
        public static final int APP_UPDATE_CODE = 7;

        public static final int APP_VERSION_RESULT_CODE = 1001;
        public static final int ACCOUNT_VERSION_RESULT_NEW_VERSION_CODE = 1002;
        public static final int ACCOUNT_VERSION_RESULT_INVALID_CODE = 1001;
        public static final int ACCOUNT_VERSION_RESULT_OK_CODE = 1000;
    }


}
