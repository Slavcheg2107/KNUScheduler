package com.example.krasn.knuscheduler.Events;

/**
 * Created by krasn on 9/4/2017.
 */

public class ErrorEvent {
    private String error;
    public ErrorEvent(String error){
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
