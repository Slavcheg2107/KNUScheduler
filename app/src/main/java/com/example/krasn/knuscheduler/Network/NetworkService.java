package com.example.krasn.knuscheduler.Network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.krasn.knuscheduler.ApplicationClass;
import com.example.krasn.knuscheduler.Events.ErrorEvent;
import com.example.krasn.knuscheduler.Events.GettingGroupsEvent;
import com.example.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.example.krasn.knuscheduler.Models.GroupModel.Group;
import com.example.krasn.knuscheduler.Models.GroupModel.Groups;
import com.example.krasn.knuscheduler.Models.Schedule.Schedule;
import com.example.krasn.knuscheduler.Models.Schedule.Schedules;
import com.example.krasn.knuscheduler.Models.WeekModel.Week1;
import com.example.krasn.knuscheduler.Models.WeekModel.Week2;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by krasn on 9/4/2017.
 */

public class NetworkService {
    private RetrofitConfig retrofitConfig;

    public NetworkService(){
        retrofitConfig = new RetrofitConfig();
    }

    public void getGroups() {

            Call<Groups> call = retrofitConfig.getApiNetwork().getGroups();
            call.enqueue(new Callback<Groups>() {
                @Override
                public void onResponse(@NonNull Call<Groups> call, @NonNull Response<Groups> response) {
                    if (response.isSuccessful()) {
                        Realm realm = ApplicationClass.getRealm();
                        List<Group> groupList = response.body().getGroups();

                        for (Group group : groupList) {
                            realm.beginTransaction();
                            Group group1 = realm.createObject(Group.class);
                            group1.setTitle(group.getTitle());
                            group.setWeek1(new Week1());
                            group.setWeek2(new Week2());
                            realm.commitTransaction();
                        }

                        EventBus.getDefault().post(new GettingGroupsEvent());
                    } else EventBus.getDefault().post(new ErrorEvent("getGroups not successful"));
                }

                @Override
                public void onFailure(@NonNull Call<Groups> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        }

    public void getSchedule(final String group) {

            Call<Schedules> call = retrofitConfig.getApiNetwork().getSchedule(group);
            call.enqueue(new Callback<Schedules>() {
                @Override
                public void onResponse(@NonNull Call<Schedules> call, @NonNull Response<Schedules> response) {
                    if (response.isSuccessful()) {
                        List<Schedule> week1Schedule = new ArrayList<Schedule>();
                        List<Schedule> week2Schedule = new ArrayList<Schedule>();
                        Schedules schedules = response.body();
                        if (schedules != null) {
                            for (Schedule schedule_ : schedules.getSchedule()) {
                                if (schedule_.getWeek() == 1) {
                                    week1Schedule.add(schedule_);
                                } else week2Schedule.add(schedule_);
                            }
                        }
                        Realm realm = ApplicationClass.getRealm();
                        Week1 week1 = new Week1(week1Schedule);
                        Week2 week2 = new Week2(week2Schedule);
                        realm.beginTransaction();
                        Week1 managedWeek1 = realm.copyToRealm(week1);
                        Week2 managedWeek2 = realm.copyToRealm(week2);
                        Group group1 = realm.where(Group.class).equalTo("title", group).findFirst();
                        group1.setWeek1(managedWeek1);
                        group1.setWeek2(managedWeek2);
                        realm.commitTransaction();
                        EventBus.getDefault().post(new GettingScheduleEvent(group));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Schedules> call, Throwable t) {
                    Log.e("getSchedule", t.getMessage());
                }
            });
        }
    }
