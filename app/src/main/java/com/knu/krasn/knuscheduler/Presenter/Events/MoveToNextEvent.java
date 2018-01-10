package com.knu.krasn.knuscheduler.Presenter.Events;

/**
 * Created by krasn on 9/16/2017.
 */

public class MoveToNextEvent {
    private String message;

    public MoveToNextEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
