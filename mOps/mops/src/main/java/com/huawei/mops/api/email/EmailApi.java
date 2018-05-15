package com.huawei.mops.api.email;

import android.content.Context;

import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.OperateResult;
import com.huawei.mops.bean.PageInfo;
import com.huawei.mops.components.retrofit.RetrofitUtils;

import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tWX366549 on 2016/12/7.
 */
public class EmailApi {

    private EmailService emailService;

    public EmailApi(Context context) {
        emailService = RetrofitUtils.getRetrofit(context).create(EmailService.class);
    }

    public Observable<OkResponse<PageInfo<NoticeEmailEntity>>> getEmailByPage(Map<String, Object> params) {
        return emailService.getEmailByPage(params).subscribeOn(Schedulers.newThread());
    }

    public Observable<OperateResult> saveEmailInfo(Map<String, Object> params) {
        return emailService.saveEmailInfo(params).subscribeOn(Schedulers.newThread());
    }

    public Observable<OperateResult> deleteEmailInfo(int id) {
        return emailService.deleteEmailInfo(id).subscribeOn(Schedulers.newThread());
    }

}

