package com.huawei.mops.util;

import com.huawei.mops.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sll on 2015/8/21.
 */
public class Constants {

    public static final String BASE_URL = BuildConfig.SERVER_HOST + "/CSICLab/";
    public static String Cookie = "";

    public static final int BUSINESS = 3;

    public static final Map<Integer, String> mNavMap = new HashMap<>();

    public static final String UPDATE_URL = "http://www.pursll.com/update.json";

    public static final String IMAGE_CACHE_PATH = "imageloader/Cache";

    public static final int CONNECT_TIME_OUT = 30;

    public static final int POPUP_MODULE = 100;
    public static final int POPUP_REGION = 101;
    public static final int POPUP_CSIC = 102;
    public static final int POPUP_EMAIL_RECEIVER = 103;
    public static final int POPUP_EMAIL_CC = 104;

    public static final String URL_NOTICE_BREAKDOWN_DELETE = BASE_URL + "notice/breakdown/deleteInfo.do";
    public static final String URL_NOTICE_BREAKDOWN_PUBLISH = BASE_URL + "notice/breakdown/sendBreakdownNotice.do";
    public static final String URL_NOTICE_BREAKDOWN_COPY = BASE_URL + "notice/breakdown/copyNotice.do";

    public static final String URL_NOTICE_CSIC_UPDATE_DELETE = BASE_URL + "notice/csicUpdate/deleteInfo.do";
    public static final String URL_NOTICE_CSIC_UPDATE_PUBLISH = BASE_URL + "notice/csicUpdate/sendCsicNotice.do";
    public static final String URL_NOTICE_CSIC_UPDATE_COPY = BASE_URL + "notice/csicUpdate/copyNotice.do";

    public static final String URL_NOTICE_WARNING_DELETE = BASE_URL + "notice/warning/deleteInfo.do";
    public static final String URL_NOTICE_WARNING_PUBLISH = BASE_URL + "notice/warning/sendWarningNotice.do";
    public static final String URL_NOTICE_WARNING_COPY = BASE_URL + "notice/warning/copyNotice.do";

    public static final String URL_NOTICE_PLATFORM_UPDATE_DELETE = BASE_URL + "notice/platformUpdate/deleteInfo.do";
    public static final String URL_NOTICE_PLATFORM_UPDATE_PUBLISH = BASE_URL + "notice/platformUpdate/sendPlatformNotice.do";
    public static final String URL_NOTICE_PLATFORM_UPDATE_COPY = BASE_URL + "notice/platformUpdate/copyNotice.do";

    public static final String URL_EMAIL_DELETE = BASE_URL + "notice/email/deleteEmailInfo.do";

    public static final String APK_DOWNLOAD_URL = "fileOperate/ignore/m5/app/downloadAppFile.do";

}
