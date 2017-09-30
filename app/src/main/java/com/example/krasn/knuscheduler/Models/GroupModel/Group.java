package com.example.krasn.knuscheduler.Models.GroupModel;

import com.example.krasn.knuscheduler.Models.WeekModel.Week1;
import com.example.krasn.knuscheduler.Models.WeekModel.Week2;

import io.realm.RealmObject;

public class Group extends RealmObject{
    private String title;
    private Week1 week1;
    private Week2 week2;
    public Group(){}
    public Group(String title){
        this.title = title;
    }


    public Week1 getWeek1() {
        return week1;
    }

    public void setWeek1(Week1 week1) {
        this.week1 = week1;
    }

    public Week2 getWeek2() {
        return week2;
    }

    public void setWeek2(Week2 week2) {
        this.week2 = week2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}