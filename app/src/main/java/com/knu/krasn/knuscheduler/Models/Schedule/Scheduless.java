package com.knu.krasn.knuscheduler.Models.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by krasn on 9/26/2017.
 */

public class Scheduless {

        @SerializedName("schedule")
        @Expose
        public static List<Schedule> schedule = null;
        public static void setSchedules(List<Schedule> schedules){
            Scheduless.schedule = schedules;
        }
        public static List<Schedule> getSchedule(){
            return schedule;
        }
    }
