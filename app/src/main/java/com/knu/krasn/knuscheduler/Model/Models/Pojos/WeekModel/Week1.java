package com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;


public class Week1 extends RealmObject {
    private RealmList<Schedule> schedules = new RealmList<>();
    private DayOfWeek day1 = new DayOfWeek(1);
    private DayOfWeek day2 = new DayOfWeek(2);
    private DayOfWeek day3 = new DayOfWeek(3);
    private DayOfWeek day4 = new DayOfWeek(4);
    private DayOfWeek day5 = new DayOfWeek(5);
    private DayOfWeek day6 = new DayOfWeek(6);
    public Week1() {
    }

    public Week1(List<Schedule> week1Schedule) {
        schedules.addAll(week1Schedule);
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void setSchedules(List<Schedule> schedules) {

    }

    public void addScheduleToWeek1(Schedule schedule_) {
        this.schedules.add(schedule_);
    }

    public List<DayOfWeek> getDays(){
        Realm realm = ApplicationClass.getRealm();
        realm.beginTransaction();
        for(Schedule schedule : schedules){
            if(schedule.getDay() == 1) {
                if (!day1.getScheduleList().contains(schedule))
                    day1.addSchedule(schedule);

            }
            else if(schedule.getDay() == 2){
                if (!day2.getScheduleList().contains(schedule))
                    day2.addSchedule(schedule);
            }
            else if(schedule.getDay() == 3){
                if (!day3.getScheduleList().contains(schedule))
                    day3.addSchedule(schedule);
            }
            else if(schedule.getDay() == 4){
                if (!day4.getScheduleList().contains(schedule))
                    day4.addSchedule(schedule);
            } else if(schedule.getDay() == 5){
                if (!day5.getScheduleList().contains(schedule))
                    day5.addSchedule(schedule);
            } else if (schedule.getDay() == 6) {
                if (!day6.getScheduleList().contains(schedule))
                    day6.addSchedule(schedule);
            }
        }
        List<DayOfWeek> days = new ArrayList<>();
        if(day1.getScheduleSize()!=0) {
            days.add(day1);
        }
        if(day2.getScheduleSize()!=0) {
            days.add(day2);
        }
        if(day3.getScheduleSize()!=0) {
            days.add(day3);
        }
        if(day4.getScheduleSize()!=0) {
            days.add(day4);
        }
        if(day5.getScheduleSize()!=0) {
            days.add(day5);
        }
        if (day6.getScheduleSize() != 0) {
            days.add(day6);
        }
        realm.commitTransaction();
        return days;
    }
}
