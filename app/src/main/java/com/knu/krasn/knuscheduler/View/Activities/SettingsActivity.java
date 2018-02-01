package com.knu.krasn.knuscheduler.View.Activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Toast;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.AppPreferencesActivity;

import java.util.Calendar;

import geek.owl.com.ua.KNUSchedule.R;

import static com.knu.krasn.knuscheduler.ApplicationClass.settings;

/**
 * Created by krasn on 12/2/2017.
 */
public class SettingsActivity extends AppPreferencesActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.setting_title);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferencesFragment()).commit();
    }

    public static class MainPreferencesFragment extends PreferenceFragment {
        static Preference currentWeek;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.prefs_main);
            currentWeek = findPreference(getString(R.string.key_current_week));

            // current week change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_current_week)));

            // notification preference change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications)));

            // feedback preference click listener
            Preference goToMarket = findPreference(getString(R.string.key_go_to_market));
            goToMarket.setOnPreferenceClickListener(preference -> {
                goToMarket(getActivity());
                return true;
            });
            // current group change listener
            Preference changeGroup = findPreference(getString(R.string.key_reload));
            changeGroup.setOnPreferenceClickListener(preference -> showDialog());

        }

        private boolean showDialog() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.are_you_sure);
            dialog.setCancelable(true);
            dialog.setPositiveButton(R.string.yes, (dialog12, which) -> {

                settings.edit().putString(getString(R.string.current_group), "").apply();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra(getString(R.string.key_reload), " ");
                startActivity(i);
                getActivity().finish();
            });
            dialog.setNegativeButton(R.string.no, (dialog1, which) -> dialog1.dismiss());
            dialog.show();
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        if (preference instanceof CheckBoxPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    settings.getBoolean(preference.getKey(), true));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    settings.getString(preference.getKey(), ""));
        }
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, newValue) -> {
        String stringValue = newValue.toString();

        boolean notificationsEnabled = true;
        if (preference instanceof ListPreference) {
            Calendar cal = Calendar.getInstance();

            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            settings.edit().putInt(ApplicationClass.getContext().getResources().getString(R.string.key_current_week_of_year)
                    , cal.get(Calendar.WEEK_OF_YEAR)).apply();
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference boxPreference = (CheckBoxPreference) preference;

            notificationsEnabled = !notificationsEnabled;
        }

        return true;
    };

    @Override
    public void onBackPressed() {
        String s = settings.getString(getString(R.string.key_current_week), getString(R.string.current_week));
        if (!s.equals(getString(R.string.choose_week_title))) {
            startActivity(new Intent(this, ScheduleActivity.class));
            finish();

        } else {
            Toast.makeText(this, R.string.did_not_choose_week, Toast.LENGTH_LONG).show();
        }
    }

    private static void goToMarket(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(i, null));
    }


}

