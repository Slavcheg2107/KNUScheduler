package com.knu.krasn.knuscheduler.Presenter.Events;

/**
 * Created by krasn on 9/8/2017.
 */

public class GettingScheduleEvent {
    private String message;

    public GettingScheduleEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
