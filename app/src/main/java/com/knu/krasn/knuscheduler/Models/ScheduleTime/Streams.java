package com.knu.krasn.knuscheduler.Models.ScheduleTime;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;


public class Streams {

    @SerializedName("data")
    @Expose
    private RealmList<ScheduleTime> data = new RealmList<>();

    public List<ScheduleTime> getData() {
        return data;
    }

    public void setData(RealmList<ScheduleTime> data) {
        this.data = data;
    }

}