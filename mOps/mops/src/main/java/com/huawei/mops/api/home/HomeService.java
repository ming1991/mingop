package com.huawei.mops.api.home;

import com.huawei.mops.bean.ApkUpgradeInfo;
import com.huawei.mops.bean.AppUpdateInfo;
import com.huawei.mops.bean.BannerInfo;
import com.huawei.mops.bean.ContactInfo;
import com.huawei.mops.bean.EachProInfo;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/27.
 */
public interface HomeService {

    @POST("fileOperate/ignore/m5/app/getCyclePictureUrl.do")
    Observable<BannerInfo> getBanners();

    @POST("")
    @FormUrlEncoded
    Observable<Map<String, EachProInfo>> getEachProInfos(@FieldMap Map<String, String> params);

    @POST("")
    @FormUrlEncoded
    Observable<ContactInfo> getContactInfo(@FieldMap Map<String, String> params);

    @POST("logout.do")
    Observable<String> logout();

    @POST("fileOperate/ignore/m5/app/checkAppVersion.do")
    @FormUrlEncoded
    Observable<AppUpdateInfo> checkApkUpdate(@FieldMap Map<String, Object> params);
}
