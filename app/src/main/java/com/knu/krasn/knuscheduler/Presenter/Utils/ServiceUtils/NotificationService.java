package com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;
import com.knu.krasn.knuscheduler.View.Activities.ScheduleActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import geek.owl.com.ua.KNUSchedule.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by krasn on 11/23/2017.
 */

public class NotificationService extends IntentService {
    private static final int MY_NOTIFICATION_ID = 1;
    public static String currentGroup;
    public static boolean isRunning = false;
    final String TAG = "NOTIFICATIONSERVICE";
    NotificationManager notificationManager;
    private Disposable dis;

    public NotificationService() {
        super("com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.e(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e(TAG, "onHandle");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        currentGroup = intent != null ? intent.getStringExtra(getString(R.string.current_group)) : null;
        isRunning = true;
        startService();
        return START_STICKY;

    }

    public void startService() {
        dis = Observable.interval(60 * 10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> showNotification());

    }

    private void showNotification() {
        Log.e(TAG, "StartService");
        Schedule nextSchedule = getNextSchedule();
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_content);
        if (nextSchedule != null) {

            remoteViews.setTextViewText(R.id.lesson_type, nextSchedule.getDiscipline());
            remoteViews.setTextViewText(R.id.lesson_time, nextSchedule.getBeginTime() + " " + nextSchedule.getEndTime());
            remoteViews.setTextViewText(R.id.teacher, nextSchedule.getTeachers());
            remoteViews.setTextViewText(R.id.room, nextSchedule.getRoom());
            remoteViews.setImageViewResource(R.id.imageView2, R.drawable.runersilhouetterunningfast);

            Intent notificationIntent = new Intent(getApplicationContext(), ScheduleActivity.class);
            notificationIntent.putExtra(getString(R.string.current_group), currentGroup);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(getApplicationContext(), "worker1")
                    .setSmallIcon(R.drawable.ic_week2_tab)
                    .setContentTitle("Run Forest, Run")
                    .setContent(remoteViews)
                    .setContentText("Run Forest, Run")
                    .setContentIntent(contentIntent)
                    .build();
            if (notificationManager != null) {
                notificationManager.notify(MY_NOTIFICATION_ID, notification);
            }
        }
    }

    private Schedule getNextSchedule() {
        Schedule nextSchedule = null;
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.key_notifications), true)) {

            if (currentGroup != null) {
                Group group = ApplicationClass.getRealm()
                        .where(Group.class)
                        .equalTo("title", currentGroup)
                        .findFirst();

                String currentWeek = PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getContext())
                        .getString(getString(R.string.key_current_week), " ");
                Calendar cal = Calendar.getInstance();
                int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
                List<Schedule> schedules = new ArrayList<>();
                try {
                    List<String> weeks = Arrays.asList(getResources().getStringArray(R.array.week_selection));
                    String week1 = weeks.get(0);
                    String week2 = weeks.get(1);
                    if (currentWeek.equals(week1)) {
                        if (dayNumber >= 2 && dayNumber <= 6) {
                            if (group != null) {

                                for (DayOfWeek dayOfWeek : group.getWeek1().getDays()) {
                                    if (dayOfWeek.getDayNumber() == dayNumber - 1) {
                                        schedules = dayOfWeek.getScheduleList();
                                    }
                                }
                            }
                        }
                    }
                    if (currentWeek.equals(week2)) {
                        if (dayNumber >= 2 && dayNumber <= 6) {
                            if (group != null) {
                                for (DayOfWeek dayOfWeek : group.getWeek2().getDays()) {
                                    if (dayOfWeek.getDayNumber() == dayNumber - 1) {
                                        schedules = dayOfWeek.getScheduleList();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                Date currentTime = cal.getTime();
                String curTime = DateUtils.formatDateTime(getApplicationContext(), currentTime.getTime(), DateUtils.FORMAT_SHOW_TIME);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                nextSchedule = null;
                if (schedules.size() != 0) {
                    try {
                        for (Schedule schedule : schedules) {

                            Date curDate = format.parse(curTime);
                            Date scheduleDate = format.parse(schedule.getBeginTime());

                            if (curDate.getTime() - scheduleDate.getTime() > 0 && curDate.getTime() - scheduleDate.getTime() < (1000 * 60 * 30)) {
                                nextSchedule = schedule;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return nextSchedule;
    }

    @Override
    public void onDestroy() {
        dis.dispose();
        super.onDestroy();
    }
}

