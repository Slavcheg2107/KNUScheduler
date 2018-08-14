package com.knu.krasn.knuscheduler;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by krasn on 9/26/2017.
 */

public class ApplicationClass extends Application {
    public static SharedPreferences settings;
    static SharedPreferences prefs;
    static Context context;

    public static SharedPreferences getPreferences() {
        return prefs;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        context = getApplicationContext();

    }


}