package com.knu.krasn.knuscheduler.Model.Models.Pojos.DayOfWeek;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by krasn on 10/2/2017.
 */

public class DayOfWeek extends RealmObject {
    private RealmList<Schedule> scheduleList =new RealmList<>();
    private Integer dayNumber;

    public DayOfWeek() {
    }

    public DayOfWeek(Integer dayNumber,List<Schedule> scheduleList) {
        this.scheduleList.addAll(scheduleList);
        this.dayNumber = dayNumber;
    }
    public DayOfWeek(Integer dayNumber){
        this.dayNumber = dayNumber;
    }
    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Integer getScheduleSize(){
        if(scheduleList!=null){
            return scheduleList.size();
        }
        else return 0;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList.addAll(scheduleList);
    }

    public void addSchedule(Schedule schedule) {
        scheduleList.add(schedule);
    }


    public void clearSchedule(){
        this.scheduleList.clear();
    }
}
