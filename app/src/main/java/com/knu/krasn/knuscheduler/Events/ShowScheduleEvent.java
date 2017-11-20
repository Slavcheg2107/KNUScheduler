package com.knu.krasn.knuscheduler.Events;

/**
 * Created by krasn on 10/4/2017.
 */

public class ShowScheduleEvent {
    private boolean isShown;
    private int dayNumber;
    private String adapterName;



    public ShowScheduleEvent(boolean isShown, int dayNumber, String adapterName){
        this.isShown = isShown;
        this.dayNumber = dayNumber;
        this.adapterName = adapterName;

    }

    public String getAdapterName() {
        return adapterName;
    }
    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}
