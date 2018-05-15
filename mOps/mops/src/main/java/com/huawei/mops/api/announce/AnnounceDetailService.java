package com.huawei.mops.api.announce;

import com.huawei.mops.bean.OperateResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tWX366549 on 2016/12/6.
 */
public interface AnnounceDetailService {
    @POST("")
    @FormUrlEncoded
    Observable<OperateResult> deleteAnnounceById(@Field("id") int id);

    @POST("")
    @FormUrlEncoded
    Observable<OperateResult> publishAnnounceById(@Field("id") int id);


}
