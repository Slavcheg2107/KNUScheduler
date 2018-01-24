package com.knu.krasn.knuscheduler.Presenter.Network;


import android.util.Log;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties.Faculties;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties.Faculty;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Groups;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedules;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel.Week1;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel.Week2;
import com.knu.krasn.knuscheduler.Presenter.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GetFacultiesEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingScheduleEvent;
import com.mindorks.nybus.NYBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geek.owl.com.ua.KNUSchedule.R;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NotificationService.currentGroup;


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

    public void getFaculties() {
        retrofitConfig.getApiNetwork().getFaculties(headerMap)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new SingleObserver<Faculties>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Faculties faculties) {
                        Realm realm = ApplicationClass.getRealm();
                        realm.beginTransaction();
                        List<Faculty> facultyList = faculties.getFaculties();
                        for (Faculty faculty : facultyList) {
                            Faculty faculty1 = realm.createObject(Faculty.class);
                            faculty1.setId(faculty.getId());
                            faculty1.setName(faculty.getName());
//                    String title = faculty.getName();
//                    realm.copyToRealm(faculty);

                        }
                        realm.commitTransaction();
                        NYBus.get().post(new GetFacultiesEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage().contains("timeout")) {
                            getFaculties();
                        }
                    }
                });
    }

    public void getGroups(String facultyID) {
        mCompositeDisposable.add(retrofitConfig.getApiNetwork().getGroups(facultyID, headerMap)
                .subscribeOn(Schedulers.io())
                .map(Groups::getGroups)
                .observeOn(Schedulers.io())
                .subscribe(groups -> {
                    Realm realm = ApplicationClass.getRealm();
                    realm.beginTransaction();
                    Faculty detachedFaculty;
                    Faculty faculty = realm.where(Faculty.class).equalTo("id", facultyID).findFirst();
                    for (Group group : groups) {
                        Group group1 = realm.createObject(Group.class);
                        group1.setTitle(group.getTitle());
                        group.setWeek1(new Week1());
                        group.setWeek2(new Week2());
                        if (faculty != null) {
                            faculty.addGroup(group);
                        }
                        Log.e("TAG", group.getTitle().concat(","));
                    }
                    detachedFaculty = realm.copyFromRealm(faculty);
                    realm.commitTransaction();
                    NYBus.get().post(new GettingGroupsEvent(detachedFaculty.getGroups()));
                }, throwable -> {
                    if (throwable.getMessage().equals("timeout")) getGroups(facultyID);
                }));
    }


    public void getSchedule(final String groupTitle) {


        mCompositeDisposable.add((retrofitConfig.getApiNetwork().getSchedule(groupTitle, headerMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedules -> {
                    Realm realm = ApplicationClass.getRealm();
                    Group group1 = realm.where(Group.class).equalTo("title", groupTitle).findFirst();
                    if (group1 == null) {
                        settings.edit().putString("Reload", "reload").apply();
                        NYBus.get().post(new ErrorEvent("reload"));
                    } else {
                        realm.beginTransaction();
                        List<Schedule> week1Schedule = new ArrayList<>();
                        List<Schedule> week2Schedule = new ArrayList<>();
                        if (schedules != null) {
                            for (Schedule schedule : schedules.getSchedule()) {
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
                        NYBus.get().post(new GettingScheduleEvent(groupTitle));

                        settings.edit().putString(ApplicationClass.getContext().getString(R.string.current_group), groupTitle).apply();
                        currentGroup = groupTitle;
                    }

                }, throwable -> {
                    if (throwable.getMessage().equals("timeout")) {
                        getSchedule(groupTitle);
                    }
                }));
    }

    public Single<List<Schedule>> getSearchQuery(String searchQuery, int limit, int offset) {

        return retrofitConfig.getApiNetwork().getSearchingSchedule(searchQuery, limit, offset, headerMap)
                .map(Schedules::getSchedule)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(throwable -> getSearchQuery(searchQuery, limit, offset));


    }
}


