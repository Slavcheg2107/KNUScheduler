package com.knu.krasn.knuscheduler;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by krasn on 9/26/2017.
 */

public class ApplicationClass extends Application {
    public static SharedPreferences settings;
    static SharedPreferences prefs;
    static Context context;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("KNUSchedule.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        context = getApplicationContext();

    }


}