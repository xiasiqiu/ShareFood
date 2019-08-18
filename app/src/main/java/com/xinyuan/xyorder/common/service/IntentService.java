package com.xinyuan.xyorder.common.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.common.bean.NoticeData;
import com.xinyuan.xyorder.common.bean.PushBean;
import com.xinyuan.xyorder.common.bean.PushData;
import com.xinyuan.xyorder.main.MainActivity;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;

/**
 * Created by f-x on 2017/11/610:24
 */

public class IntentService extends GTIntentService {
    private MediaPlayer mp;//mediaPlayer对象

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        XLog.v("onReceiveClientId -> " + "clientid = " + s);
        if (!XEmptyUtils.isEmpty(s)) {
            MyApplication.cid = s;
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        XLog.v("GTTransmitMessage -> " + "GTTransmitMessage = " + msg);
        byte[] payload = msg.getPayload();
        if (payload == null) {
        } else {
            String data = new String(payload);
            XLog.v(data);
            PushData pushData = new PushData();
            Gson gson = new Gson();
            pushData = gson.fromJson(data, PushData.class);

            showNotification(getApplicationContext(), 0, pushData.getContent(), pushData.getTitle());
            switch (pushData.getType()) {
                case "notice":

                    break;
            }
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    private void showNotification(Context context, int id, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_start_logo);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}
