package com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils;

/**
 * Created by krasn on 9/30/2017.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkConnectionChecker {
    public Context context;

    public NetworkConnectionChecker(Context applicationContext) {
        this.context=applicationContext;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}