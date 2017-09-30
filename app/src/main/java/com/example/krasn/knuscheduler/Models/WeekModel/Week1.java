package com.example.krasn.knuscheduler.Models.WeekModel;

import com.example.krasn.knuscheduler.Models.Schedule.Schedule;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Week1 extends RealmObject {
    private RealmList<Schedule> schedules = new RealmList<>();

    public Week1() {
    }

    public Week1(List<Schedule> week1Schedule) {
        for (Schedule schedule : week1Schedule) {
            schedules.add(schedule);
        }
    }

//    public List<Schedule> schedule_list = new ArrayList<>();

    public void setSchedules(List<Schedule> schedules) {

    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void addScheduleToWeek1(Schedule schedule_) {

        this.schedules.add(schedule_);
    }
}
