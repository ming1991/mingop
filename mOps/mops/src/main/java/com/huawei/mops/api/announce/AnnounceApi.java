package com.huawei.mops.api.announce;

import android.content.Context;

import com.huawei.mops.bean.AnnounceNoticeInfo;
import com.huawei.mops.bean.NoticeCategoryEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tWX366549 on 2016/10/28.
 */
public class AnnounceApi {

    private AnnounceManagerService announceManagerService;

    public AnnounceApi(Context context) {
        announceManagerService = RetrofitUtils.getRetrofit(context).create(AnnounceManagerService.class);
    }

    public Observable<Map<String, AnnounceNoticeInfo>> getAnnounceStatisticsData(Map<String, String> params) {
        return announceManagerService.getAnnounceStatisticsData(params).subscribeOn(Schedulers.newThread());
    }

    public Observable<OkResponse<List<NoticeCategoryEntity>>> getAnnounceCategories() {
        return announceManagerService.getAnnounceCategories();
    }
}
