package com.huawei.mops.api.announce;

import com.huawei.mops.bean.AnnounceNoticeInfo;
import com.huawei.mops.bean.NoticeCategoryEntity;
import com.huawei.mops.bean.OkResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/28.
 */
public interface AnnounceManagerService {

    @POST("")
    @FormUrlEncoded
    Observable<Map<String, AnnounceNoticeInfo>> getAnnounceStatisticsData(@FieldMap Map<String, String> params);

    @POST("notice/category/getAllCategory.do")
    Observable<OkResponse<List<NoticeCategoryEntity>>> getAnnounceCategories();
}
