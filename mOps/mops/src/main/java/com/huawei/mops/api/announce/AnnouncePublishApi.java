package com.huawei.mops.api.announce;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.huawei.mops.MopsApp;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.bean.SendNoticeInfo;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tWX366549 on 2016/11/11.
 */
public class AnnouncePublishApi {

    private AnnouncePublishService publishService;
    private static final int SAVE = 1;
    private static final int PUBLISH = 2;
    private Gson gson = new Gson();

    public AnnouncePublishApi(Context context) {
        publishService = RetrofitUtils.getRetrofit(context).create(AnnouncePublishService.class);
    }

    public Observable<OkResponse<List<RegionInfo>>> getAllRegions() {
        return publishService.getAllRegions("yes");
    }

    public Observable<OkResponse<List<CSICInfo>>> getCsicInfo(int regionId) {
        return publishService.getCsicInfo(regionId,"yes");
    }

    public Observable<OkResponse<List<NoticeEmailEntity>>> getAllEmailInfos() {
        return publishService.getAllEmailInfos();
    }

    public Observable<OperateResult> publishBreakdownNotice(NoticeBreakdownEntity breakdownNotice) {
        return publishService.publishBreakdownNotice(createParams(PUBLISH, breakdownNotice));
    }

    public Observable<OperateResult> publishCsicUpdateNotice(NoticeCsicUpdateEntity csicUpdateNotice) {
        return publishService.publishCsicUpdateNotice(createParams(PUBLISH, csicUpdateNotice));
    }

    public Observable<OperateResult> publishWarningNotice(NoticeWarningEntity warningNotice) {
        return publishService.publishWarningNotice(createParams(PUBLISH, warningNotice));
    }

    public Observable<OperateResult> publishPlatformUpdateNotice(NoticePlatformUpdateEntity platformUpdateNotice) {
        return publishService.publishPlatformUpdateNotice(createParams(PUBLISH, platformUpdateNotice));
    }

    public Observable<OperateResult> saveOrUpdateBreakdownNotice(NoticeBreakdownEntity breakdownNotice) {
        return publishService.publishBreakdownNotice(createParams(SAVE, breakdownNotice));
    }

    public Observable<OperateResult> saveOrUpdateCsicUpdateNotice(NoticeCsicUpdateEntity csicUpdateNotice) {
        return publishService.publishCsicUpdateNotice(createParams(SAVE, csicUpdateNotice));
    }


    public Observable<OperateResult> saveOrUpdateWarningNotice(NoticeWarningEntity warningNotice) {
        return publishService.publishWarningNotice(createParams(SAVE, warningNotice));
    }

    public Observable<OperateResult> saveOrUpdatePlatformUpdateNotice(NoticePlatformUpdateEntity platformUpdateNotice) {
        return publishService.publishPlatformUpdateNotice(createParams(SAVE, platformUpdateNotice));
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> saveEmailInfo(NoticeEmailEntity emailEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", emailEntity.getId());
        params.put("groupName", emailEntity.getGroupName());
        params.put("sendToPerson", emailEntity.getSendToPerson());
        params.put("copyToPerson", emailEntity.getCopyToPerson());
        params.put("remarks", emailEntity.getRemarks());
        return publishService.saveEmailInfo(params).subscribeOn(Schedulers.newThread());
    }

    @NonNull
    private Map<String, Object> createParams(int type, BaseNoticeEntity noticeEntity) {
        SendNoticeInfo sendNoticeInfo = new SendNoticeInfo();
        sendNoticeInfo.setAction(type);
        sendNoticeInfo.setLanguage(MopsApp.language);
        sendNoticeInfo.setNoticeEntity(noticeEntity);
        String str = gson.toJson(sendNoticeInfo);
        Map<String, Object> p = new HashMap<>();
        p.put("params", str);
        return p;
    }

/*    public Observable<OperateResult> publishBreakdownNotice(NoticeBreakdownEntity breakdownNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", PUBLISH);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(breakdownNotice));
        return publishService.publishBreakdownNotice(params);
    }

    public Observable<OperateResult> publishCsicUpdateNotice(NoticeCsicUpdateEntity csicUpdateNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", PUBLISH);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(csicUpdateNotice));
        return publishService.publishCsicUpdateNotice(params);
    }

    public Observable<OperateResult> publishWarningNotice(NoticeWarningEntity warningNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", PUBLISH);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(warningNotice));
        return publishService.publishWarningNotice(params);
    }

    public Observable<OperateResult> publishPlatformUpdateNotice(NoticePlatformUpdateEntity platformUpdateNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", PUBLISH);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(platformUpdateNotice));
        return publishService.publishPlatformUpdateNotice(params);
    }

    public Observable<OperateResult> saveOrUpdateBreakdownNotice(NoticeBreakdownEntity breakdownNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", SAVE);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(breakdownNotice));
        return publishService.publishBreakdownNotice(params);
    }

    public Observable<OperateResult> saveOrUpdateCsicUpdateNotice(NoticeCsicUpdateEntity csicUpdateNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", SAVE);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(csicUpdateNotice));
        return publishService.publishCsicUpdateNotice(params);
    }

    public Observable<OperateResult> saveOrUpdateWarningNotice(NoticeWarningEntity warningNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", SAVE);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(warningNotice));
        return publishService.publishWarningNotice(params);
    }

    public Observable<OperateResult> saveOrUpdatePlatformUpdateNotice(NoticePlatformUpdateEntity platformUpdateNotice) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", SAVE);
        params.put("language", MopsApplication.language);
        params.put("params", JSON.toJSONString(platformUpdateNotice));
        return publishService.publishPlatformUpdateNotice(params);
    }*/

}
