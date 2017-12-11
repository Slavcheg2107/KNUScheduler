package com.knu.krasn.knuscheduler.ServiceUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.knu.krasn.knuscheduler.Models.Schedule.Schedule;
import com.knu.krasn.knuscheduler.ScheduleActivity;

import geek.owl.com.ua.KNUSchedule.R;


/**
 * Created by krasn on 11/23/2017.
 */

public class Notification extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startService();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startService(Schedule schedule) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_content);
        remoteViews.setTextViewText(R.id.lesson_type, schedule.getDiscipline());
        remoteViews.setTextViewText(R.id.lesson_time, schedule.getTime().getBegin());
        remoteViews.setTextViewText(R.id.teacher, schedule.getTeachers());
        Intent notificationIntent = new Intent(this, ScheduleActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "911")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Скоро пара!")
                .setContent(remoteViews)
                .setContentIntent(contentIntent);
        builder.build().notify();
    }
}
