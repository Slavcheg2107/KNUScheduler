package com.knu.krasn.knuscheduler.Presenter.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Button;

import com.knu.krasn.knuscheduler.ApplicationClass;

import geek.owl.com.ua.KNUSchedule.R;


/**
 * Created by Bohdan on 6/19/2017.
 */

public class AppRater {
    private final static String APP_PNAME = "com.knu.krasn.knuscheduler";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 1;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = ApplicationClass.getPreferences();
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

    private static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.rate_app_dialog);
        dialog.setTitle("Оцініть наш додаток");
        Button b1 = dialog.findViewById(R.id.positive_button);
        b1.setOnClickListener(v -> {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
            dialog.dismiss();
        });

        Button b2 = dialog.findViewById(R.id.neutral);
        b2.setOnClickListener(v -> {
            if (editor != null) {
                editor.putLong("launch_count", 0);
                editor.putLong("date_firstlaunch", 0);
                editor.commit();
            }
            dialog.dismiss();
        });

        Button b3 = dialog.findViewById(R.id.negative_button);
        b3.setOnClickListener(v -> {
            if (editor != null) {
                editor.putBoolean("dontshowagain", true);
                editor.commit();
            }
            dialog.dismiss();
        });
        dialog.show();
    }
}