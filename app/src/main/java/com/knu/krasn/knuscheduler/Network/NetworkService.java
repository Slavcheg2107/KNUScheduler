package com.knu.krasn.knuscheduler.Network;


import android.content.SharedPreferences;
import android.util.Log;


import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Models.GroupModel.Group;
import com.knu.krasn.knuscheduler.Models.GroupModel.Groups;
import com.knu.krasn.knuscheduler.Models.Schedule.Schedule;
import com.knu.krasn.knuscheduler.Models.ScheduleTime.ScheduleTime;
import com.knu.krasn.knuscheduler.Models.WeekModel.Week1;
import com.knu.krasn.knuscheduler.Models.WeekModel.Week2;
import com.mindorks.nybus.NYBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;


/**
 * Created by krasn on 9/4/2017.
 */

public class NetworkService {
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private RetrofitConfig retrofitConfig;
    private Map<String, String> headerMap = new HashMap<>();

    public NetworkService() {
        retrofitConfig = new RetrofitConfig();
        headerMap.put("Username", "android");
        headerMap.put("Authorization", "Token 6369aebbfac97f6c2ae09f0b6cdde90d4cfa0718");

    }

    public void getGroups() {
        mCompositeDisposable.add(retrofitConfig.getApiNetwork().getGroups(headerMap)
                .subscribeOn(Schedulers.io())
                .map(Groups::getGroups)
                .observeOn(Schedulers.io())
                .subscribe(groups -> {
                    Realm realm = ApplicationClass.getRealm();
                    for (Group group : groups) {
                        realm.beginTransaction();
                        Group group1 = realm.createObject(Group.class);
                        group1.setTitle(group.getTitle());
                        group.setWeek1(new Week1());
                        group.setWeek2(new Week2());
                        realm.commitTransaction();
                        Log.e("TAG", group.getTitle().concat(","));
                    }
                    NYBus.get().post(new GettingGroupsEvent());
                }, throwable -> {
                    if (throwable.getMessage().equals("timeout")) getGroups();
                }));
    }


    public void getSchedule(final String group) {

        RealmList<ScheduleTime> timeRealmList = new RealmList<>();
        mCompositeDisposable.add(Single.zip(retrofitConfig.getApiNetwork().getSchedule(group, headerMap), retrofitConfig.getApiNetwork().getTime(headerMap),
                (schedules, streams) -> {
                    timeRealmList.addAll(streams.getData());
                    return schedules;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedules -> {
                    Realm realm = ApplicationClass.getRealm();
                    Group group1 = realm.where(Group.class).equalTo("title", group).findFirst();
                    if (group1 == null) {
                        SharedPreferences.Editor sharedPreferences = ApplicationClass.getPreferences().edit();
                        sharedPreferences.putString("Reload", "reload");
                        sharedPreferences.apply();
                        NYBus.get().post(new ErrorEvent("reload"));
                    }
                    else{
                        realm.beginTransaction();
                        List<Schedule> week1Schedule = new ArrayList<>();
                        List<Schedule> week2Schedule = new ArrayList<>();
                        if (schedules != null) {
                            for (Schedule schedule : schedules.getSchedule()) {
                                schedule.setTime(timeRealmList.get(schedule.getLesson() - 1));
                                switch (schedule.getWeek()) {
                                    case 1:
                                        week1Schedule.add(schedule);
                                        break;
                                    case 2:
                                        week2Schedule.add(schedule);
                                        break;
                                }
                            }
                        }
                        Week1 week1 = new Week1(week1Schedule);
                        Week2 week2 = new Week2(week2Schedule);
                        Week1 managedWeek1 = realm.copyToRealm(week1);
                        Week2 managedWeek2 = realm.copyToRealm(week2);
                        group1.setWeek1(managedWeek1);
                        group1.setWeek2(managedWeek2);
                        realm.commitTransaction();
                        NYBus.get().post(new GettingScheduleEvent(group));
                    }

                }, throwable -> {
                    if (throwable.getMessage().equals("timeout")) {
                        getSchedule(group);
                    }
                }));
    }
}


