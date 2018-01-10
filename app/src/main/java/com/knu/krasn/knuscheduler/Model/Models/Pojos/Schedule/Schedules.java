package com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule;

import java.util.List;

public class Schedules{

     List<Schedule> schedule = null;

    private String message;

    public List<Schedule> getSchedule() {
        return schedule;
    }


    public void setSchedule(List<Schedule> schedule1) {
        schedule = schedule1;
    }

    public Schedules withSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Schedules withMessage(String message) {
        this.message = message;
        return this;
    }



}


