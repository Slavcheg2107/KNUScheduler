package com.knu.krasn.knuscheduler.Models;

import io.realm.RealmObject;

/**
 * Created by krasn on 9/3/2017.
 */

public class Facultet extends RealmObject{
    private String title;
    public Facultet(){}
    public Facultet(String title){
    this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
