package com.huawei.mops.data.local;

import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.RegionInfo;
import com.huawei.mops.data.ContentDataSource;

import java.util.List;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/12/8.
 */
public class ContentLocalDataSource implements ContentDataSource {
    @Override
    public Observable<OkResponse<List<CSICInfo>>> getCsicInfo() {
        return null;
    }

    @Override
    public Observable<OkResponse<List<RegionInfo>>> getAllRegions() {
        return null;
    }

    @Override
    public Observable<OkResponse<List<NoticeEmailEntity>>> getAllEmailInfos() {
        return null;
    }
}
