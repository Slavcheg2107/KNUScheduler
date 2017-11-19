package com.knu.krasn.knuscheduler.Events;

/**
 * Created by krasn on 10/4/2017.
 */

public class ShowScheduleEvent {
    private boolean isShown;
    private int dayNumber;
    public ShowScheduleEvent(boolean isShown, int dayNumber){
        this.isShown = isShown;
        this.dayNumber = dayNumber;
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
