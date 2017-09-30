package com.example.krasn.knuscheduler.Events;

/**
 * Created by krasn on 9/16/2017.
 */

public class MessageEvent {
    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
