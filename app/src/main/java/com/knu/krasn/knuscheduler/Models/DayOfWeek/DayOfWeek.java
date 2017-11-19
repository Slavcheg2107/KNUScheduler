package com.knu.krasn.knuscheduler.Models.DayOfWeek;

import com.knu.krasn.knuscheduler.Models.Schedule.Schedule;

import java.util.List;
import java.util.Objects;

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
        for(Schedule schedule:scheduleList){
        this.scheduleList.add(schedule);
        }
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
        for(Schedule schedule: scheduleList)
        this.scheduleList.add(schedule);
    }

    public void addSchedule(Schedule schedule) {
        scheduleList.add(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        for (int i = 0; i < scheduleList.size(); i++) {
            if (Objects.equals(scheduleList.get(i).getDay(), schedule.getDay())) {
                scheduleList.remove(i);
            }
        }
    }

    public void clearSchedule(){
        this.scheduleList.clear();
    }
}
