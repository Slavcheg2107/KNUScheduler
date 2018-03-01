package com.knu.krasn.knuscheduler;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;

import java.util.HashMap;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by krasn on 9/26/2017.
 */

public class ApplicationClass extends Application {
    public static SharedPreferences settings;
    static SharedPreferences prefs;
    static Context context;
    private static HashMap<Integer, String> days;

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public static NetworkService getNetwork() {
        return new NetworkService();
    }

    public static SharedPreferences getPreferences() {
        return prefs;
    }

    public static Context getContext() {
        return context;
    }

    public static String getDay(Integer dayNumber) {
        return days.get(dayNumber);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("KNUSchedule.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        context = getApplicationContext();

        days = new HashMap<>();
        days.put(1, getString(R.string.Monday));
        days.put(2, getString(R.string.Tuesday));
        days.put(3, getString(R.string.Wednesday));
        days.put(4, getString(R.string.Thursday));
        days.put(5, getString(R.string.Friday));
        days.put(6, getString(R.string.Saturday));
    }

}