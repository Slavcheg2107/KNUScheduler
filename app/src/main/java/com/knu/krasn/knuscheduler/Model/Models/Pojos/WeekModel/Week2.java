package com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;


public class Week2 extends RealmObject {

    private RealmList<Schedule> schedules = new RealmList<>();
    private DayOfWeek day1 = new DayOfWeek(1);
    private DayOfWeek day2 = new DayOfWeek(2);
    private DayOfWeek day3 = new DayOfWeek(3);
    private DayOfWeek day4 = new DayOfWeek(4);
    private DayOfWeek day5 = new DayOfWeek(5);

    public Week2() {
    }

    public Week2(List<Schedule> week2Schedule) {
        schedules.addAll(week2Schedule);
    }

//    public List<Schedule> schedules = new ArrayList<>();

    public DayOfWeek getDay1() {
        return day1;
    }

    public void setDay1(DayOfWeek day1) {
        this.day1 = day1;
    }

    public DayOfWeek getDay2() {
        return day2;
    }

    public void setDay2(DayOfWeek day2) {
        this.day2 = day2;
    }

    public DayOfWeek getDay3() {
        return day3;
    }

    public void setDay3(DayOfWeek day3) {
        this.day3 = day3;
    }

    public DayOfWeek getDay4() {
        return day4;
    }

    public void setDay4(DayOfWeek day4) {
        this.day4 = day4;
    }

    public DayOfWeek getDay5() {
        return day5;
    }

    public void setDay5(DayOfWeek day5) {
        this.day5 = day5;
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = (RealmList<Schedule>) schedules;
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
//                day2.addSchedule(schedule);
            }
            else if(schedule.getDay() == 3){
                if (!day3.getScheduleList().contains(schedule))

                    day3.addSchedule(schedule);
//                day3.addSchedule(schedule);
            }
            else if(schedule.getDay() == 4){
                if (!day4.getScheduleList().contains(schedule))

                    day4.addSchedule(schedule);
//                day4.addSchedule(schedule);
            }
            else if(schedule.getDay() == 5){
                if (!day5.getScheduleList().contains(schedule))

                    day5.addSchedule(schedule);
//                day5.addSchedule(schedule);
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
        realm.commitTransaction();
        return days;
    }
}
