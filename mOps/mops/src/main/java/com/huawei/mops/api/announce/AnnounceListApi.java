package com.huawei.mops.api.announce;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.huawei.mops.MopsApp;
import com.huawei.mops.bean.BaseNoticeEntity;
import com.huawei.mops.bean.NoticeBreakdownEntity;
import com.huawei.mops.bean.NoticeCsicUpdateEntity;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.NoticePlatformUpdateEntity;
import com.huawei.mops.bean.NoticeWarningEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.bean.SendNoticeInfo;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tWX366549 on 2016/11/7.
 */
public class AnnounceListApi {

    private final AnnounceListService announceListService;
    private Gson gson = new Gson();
    private static final int SAVE = 1;
    private static final int PUBLISH = 2;

    public AnnounceListApi(Context context) {
        announceListService = RetrofitUtils.getRetrofit(context).create(AnnounceListService.class);
    }

    @SuppressWarnings("unchecked")
    public Observable<OkResponse<PageInfo<NoticeBreakdownEntity>>> getBreakdownNotices(Map<String, Object> parmas) {
        return announceListService.getBreakdownNotices(parmas).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OkResponse<PageInfo<NoticePlatformUpdateEntity>>> getPlatformUpdateNotices(Map<String, Object> params) {
        return announceListService.getPlatformUpdateNotices(params).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OkResponse<PageInfo<NoticeCsicUpdateEntity>>> getCiscUpdateNotices(Map<String, Object> parmas) {
        return announceListService.getCiscUpdateNotices(parmas).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OkResponse<PageInfo<NoticeWarningEntity>>> getWarningNotices(Map<String, Object> params) {
        return announceListService.getWarningNotices(params).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> deleteAnnounceById(String url, int id) {
        return announceListService.deleteAnnounceById(url, id).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> copyAnnounceById(String url, int id) {
        return announceListService.copyAnnounceById(url, id).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> publishAnnounceById(String url, int id, String language) {
        return announceListService.publishAnnounceById(url, id, language).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OkResponse<PageInfo<NoticeEmailEntity>>> getEmailByPage(Map<String, Object> params) {
        return announceListService.getEmailByPage(params).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> saveEmailInfo(Map<String, Object> params) {
        return announceListService.saveEmailInfo(params).subscribeOn(Schedulers.newThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> publishBreakdownNotice(NoticeBreakdownEntity breakdownNotice) {
        return announceListService.publishBreakdownNotice(createParams(PUBLISH, breakdownNotice));
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> publishCsicUpdateNotice(NoticeCsicUpdateEntity csicUpdateNotice) {
        return announceListService.publishCsicUpdateNotice(createParams(PUBLISH, csicUpdateNotice));
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> publishWarningNotice(NoticeWarningEntity warningNotice) {
        return announceListService.publishWarningNotice(createParams(PUBLISH, warningNotice));
    }

    @SuppressWarnings("unchecked")
    public Observable<OperateResult> publishPlatformUpdateNotice(NoticePlatformUpdateEntity platformUpdateNotice) {
        return announceListService.publishPlatformUpdateNotice(createParams(PUBLISH, platformUpdateNotice));
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

}
