package com.huawei.mops.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.huawei.mops.R;
import com.huawei.mops.api.upgrade.DownloadApi;
import com.huawei.mops.api.upgrade.DownloadProgressListener;
import com.huawei.mops.bean.Download;
import com.huawei.mops.ui.main.HomeActivity;
import com.huawei.mops.util.Constants;
import com.huawei.mops.util.FileUtil;
import com.huawei.mops.util.StringUtil;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;


import rx.Subscriber;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    int downloadCount = 0;

    public DownloadService() {
        super("DownloadService");
    }

    private File outputFile;

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_download)
                .setContentTitle("Mops.apk")
                .setContentText("Downloading File")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        download();
    }

    private void download() {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //不频繁发送通知，防止通知栏下拉卡顿
                int progress = (int) ((bytesRead * 100) / contentLength);
                if ((downloadCount == 0) || progress > downloadCount) {
                    Download download = new Download();
                    download.setTotalFileSize(contentLength);
                    download.setCurrentFileSize(bytesRead);
                    download.setProgress(progress);
                    sendNotification(download);
                }
            }
        };
        outputFile = new File(FileUtil.getDownloadDir(DownloadService.this), "Mops.apk");

        if (outputFile.exists()) {
            outputFile.delete();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("deviceType", "Android");
        params.put("business", 3);
        new DownloadApi(listener).downloadAPK(Constants.APK_DOWNLOAD_URL, params, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
                downloadCompleted();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
//                downloadCompleted();
                if (throwable instanceof ConnectException) {
                    sendIntent(getResources().getString(R.string.connect_failed));
                } else if (throwable instanceof SocketException) {
                    sendIntent(getResources().getString(R.string.connect_failed));
                } else if (throwable instanceof SocketTimeoutException) {
                    sendIntent(getResources().getString(R.string.connect_time_out));
                } else {
                    sendIntent(getResources().getString(R.string.update_failed));
                }
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void downloadCompleted() {
//        Download download = new Download();
//        download.setProgress(100);
//        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText(getResources().getString(R.string.download_complete));
        notificationManager.notify(0, notificationBuilder.build());

        //安装apk
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void sendNotification(Download download) {

//        sendIntent(download);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
                StringUtil.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtil.getDataSize(download.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download) {
        Intent intent = new Intent(HomeActivity.MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void sendIntent(String error) {
        Intent intent = new Intent(HomeActivity.MESSAGE_ERROR);
        intent.putExtra("error", error);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
