package com.knu.krasn.knuscheduler.Presenter.Events;

/**
 * Created by krasn on 10/4/2017.
 */

public class ShowScheduleEvent {

    private int dayNumber;
    private String adapterName;


    public ShowScheduleEvent(int dayNumber, String adapterName) {

        this.dayNumber = dayNumber;
        this.adapterName = adapterName;

    }

    public String getAdapterName() {
        return adapterName;
    }


    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}
