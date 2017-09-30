package com.example.krasn.knuscheduler.Models.WeekModel;

import com.example.krasn.knuscheduler.Models.Schedule.Schedule;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Week2 extends RealmObject {

    private RealmList<Schedule> schedules = new RealmList<>();

    public Week2() {
    }

    public Week2(List<Schedule> week2Schedule) {
        for (Schedule schedule : week2Schedule) {
            schedules.add(schedule);
        }
    }


//    public List<Schedule> schedules = new ArrayList<>();

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = (RealmList<Schedule>) schedules;
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void addScheduleToWeek1(Schedule schedule_) {

        this.schedules.add(schedule_);
    }
}
