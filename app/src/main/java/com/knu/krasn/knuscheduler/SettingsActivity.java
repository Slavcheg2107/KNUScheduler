package com.knu.krasn.knuscheduler;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.knu.krasn.knuscheduler.ServiceUtils.AppPreferencesActivity;

import geek.owl.com.ua.KNUSchedule.R;

/**
 * Created by krasn on 12/2/2017.
 */
public class SettingsActivity extends AppPreferencesActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Налаштування");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferencesFragment()).commit();
    }

    public static class MainPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.prefs_main);
            // gallery EditText change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_current_week)));

            // notification preference change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications)));

            // feedback preference click listener
            Preference goToMarket = findPreference(getString(R.string.key_go_to_market));
            goToMarket.setOnPreferenceClickListener(preference -> {
                goToMarket(getActivity());
                return true;
            });
            Preference changeGroup = findPreference(getString(R.string.key_changing_group));
            changeGroup.setOnPreferenceClickListener(preference -> {

                SharedPreferences preferences = ApplicationClass.getPreferences();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("GroupLoaded", "");
                editor.apply();
                String s = "getGroup";
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra(s, s);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
                return true;
            });
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
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), true));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, newValue) -> {
        String stringValue = newValue.toString();

        boolean notificationsEnabled = true;
        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

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

    private static void goToMarket(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(i, null));
    }

    private static boolean changeGroup(Context context) {


        return true;
    }
}

