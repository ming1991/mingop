package com.huawei.mops.api.inspection;

import android.content.Context;

import com.huawei.mops.api.BaseApi;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.ShowCaseEntity;
import com.huawei.mops.bean.ShowCaseResponse;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.SolutionTopicInfo;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/10/24.
 */
public class ShowCaseApi extends BaseApi {
    private ShowCaseService mShowCaseService;

    public ShowCaseApi(Context context) {
        mShowCaseService = RetrofitUtils.getRetrofit(context).create(ShowCaseService.class);
    }

    /**
     * 查询ShowCase列表
     *
     * @param params
     * @return
     */
    public Observable<ShowCaseResponse<ShowCaseEntity>> getShowCase(Map<String, String> params) {
        return mShowCaseService.getShowCase(params);
    }

    /**
     * 获取showcase所有地区部
     *
     * @return
     */
    public Observable<OkResponse<List<RegionInfo>>> getAllRegions() {
        return mShowCaseService.getAllRegions();
    }

    /**
     * 获取showcase所有代表处
     *
     * @param regionId 区域id，0 表示查询所有
     * @param isAll
     * @return
     */
    public Observable<OkResponse<List<CSICInfo>>> getCsicAreas(int regionId, boolean isAll) {
        return mShowCaseService.getCsicAreas(regionId);
    }

    /**
     * 获取showcase所有细分市场
     *
     * @return
     */
    public Observable<OkResponse<List<SolutionTopicInfo>>> getAllSolutionTopic() {
        return mShowCaseService.getAllSolutionTopic();
    }

}
