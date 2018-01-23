package com.knu.krasn.knuscheduler.Presenter.Events;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;

import java.util.List;

/**
 * Created by krasn on 1/20/2018.
 */

public class SearchSuccesEvent {
    List<Schedule> schedules;
    int whatToDo;

    public SearchSuccesEvent(List<Schedule> schedules, int whatToDo) {
        this.schedules = schedules;
        this.whatToDo = whatToDo;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public int WhatToDo() {
        return whatToDo;
    }
}
