package com.huawei.mops.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CsicService extends Service {
    public static final String ACTION_CLOSE = "";
    public static final String ACTION_GET = "";

    public CsicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
