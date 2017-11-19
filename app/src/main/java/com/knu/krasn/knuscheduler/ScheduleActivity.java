package com.knu.krasn.knuscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Events.UpdateAdapterEvent;
import com.knu.krasn.knuscheduler.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.Fragments.Week2Fragment;
import com.knu.krasn.knuscheduler.Utils.AppRater;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import geek.owl.com.ua.KNUSchedule.R;


public class ScheduleActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener {

    FragmentTransaction ft;
    FragmentManager manager;
    ViewPager viewPager;
    FrameLayout container;
    Week1Fragment week1Fragment;
    Week2Fragment week2Fragment;
    String week1Tag = "week1";
    String week2Tag = "week2";
    String groupTitle;
    Toolbar toolbar;
    private boolean isScheduleShown = false;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);
        AppRater.app_launched(this);
        groupTitle = getIntent().getStringExtra("group");
         toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(groupTitle);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();

        viewPager = findViewById(R.id.view_pager);
        container = findViewById(R.id.contentContainer);
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);

    }

    @Override
    protected void onPause() {
        NYBus.get().register(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.change_group) {
            SharedPreferences preferences = ApplicationClass.getPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("GroupLoaded", "");
            editor.apply();
            String s =  "getGroup";
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(s , s);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        ft = manager.beginTransaction();


        if (tabId == R.id.tab_week1) {
            week1Fragment = (Week1Fragment) getSupportFragmentManager().findFragmentByTag(week1Tag);

            if (week1Fragment == null) {
                week1Fragment = Week1Fragment.newInstance();
                ft.replace(R.id.contentContainer, week1Fragment, week1Tag).commit();
            } else {
                ft.replace(R.id.contentContainer, week1Fragment, week1Tag).commit();
            }
        } else if (tabId == R.id.tab_week2) {
            week2Fragment = (Week2Fragment) getSupportFragmentManager().findFragmentByTag(week2Tag);

            if (week2Fragment == null) {
                week2Fragment = Week2Fragment.newInstance(2);
                ft.replace(R.id.contentContainer, week2Fragment, week2Tag).commit();
            } else {
                ft.replace(R.id.contentContainer, week2Fragment, week2Tag).commit();
            }
        }
        NYBus.get().post(new ShowScheduleEvent(false, 0));
        NYBus.get().post(new UpdateAdapterEvent());
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {

    }
    
    @Subscribe
    public void onGettingScheduleEvent(GettingScheduleEvent event) {

    }
    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event){
        isScheduleShown = event.isShown();
        if(isScheduleShown){
            if(event.getDayNumber()!=0) {
                switch (event.getDayNumber()) {
                    case 1:
                        toolbar.setTitle(getString(R.string.Monday));
                        break;
                    case 2:
                        toolbar.setTitle(getString(R.string.Tuesday));
                        break;
                    case 3:
                        toolbar.setTitle(getString(R.string.Wednesday));
                        break;
                    case 4:
                        toolbar.setTitle(getString(R.string.Thursday));
                        break;
                    case 5:
                        toolbar.setTitle(getString(R.string.Friday));
                        break;
                }
            }
        }
        if( event.getDayNumber() == 0){
            toolbar.setTitle(groupTitle);
        }
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NYBus.get().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if(isScheduleShown){
            NYBus.get().post(new ShowScheduleEvent(false, 0));
        }
        else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Натисніть знову для виходу", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }
}
