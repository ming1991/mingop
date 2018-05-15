package com.huawei.mops.api.inspection;

import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.bean.ShowCaseResponse;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.SolutionTopicInfo;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public interface ShowCaseService {
    @POST("showcase/getShowCaseForSelectByPage.do")
    @FormUrlEncoded
    Observable<ShowCaseResponse<ShowCaseEntity>> getShowCase(@FieldMap Map<String, String> params);

    @POST("common/region/getAllRegions.do")
    Observable<OkResponse<List<RegionInfo>>> getAllRegions();

    @POST("csic/getAllCsicArea.do")
    @FormUrlEncoded
    Observable<OkResponse<List<CSICInfo>>> getCsicAreas(@Field("regionId") int regionId);

    @POST("solutionTopic/getAllSolutionTopic.do")
    Observable<OkResponse<List<SolutionTopicInfo>>> getAllSolutionTopic();

}
