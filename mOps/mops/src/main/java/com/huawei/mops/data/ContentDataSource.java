package com.huawei.mops.data;

import com.huawei.mops.bean.CSICInfo;
import com.huawei.mops.bean.NoticeEmailEntity;
import com.huawei.mops.bean.OkResponse;
import com.huawei.mops.bean.RegionInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by tWX366549 on 2016/12/8.
 */
public interface ContentDataSource {
    Observable<OkResponse<List<CSICInfo>>> getCsicInfo();

    Observable<OkResponse<List<RegionInfo>>> getAllRegions();

    Observable<OkResponse<List<NoticeEmailEntity>>> getAllEmailInfos();
}
