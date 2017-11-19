package com.knu.krasn.knuscheduler.Utils;

/**
 * Created by krasn on 9/30/2017.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class NetworkConnection {
    public Context context;

    public NetworkConnection(Context applicationContext) {
        this.context=applicationContext;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}