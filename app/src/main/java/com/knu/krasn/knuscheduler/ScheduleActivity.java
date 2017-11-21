package com.knu.krasn.knuscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.knu.krasn.knuscheduler.Adapters.TabAdapter;
import com.knu.krasn.knuscheduler.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.Fragments.Week2Fragment;
import com.knu.krasn.knuscheduler.Utils.AppRater;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;


public class ScheduleActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener {
    FragmentTransaction ft;
    FragmentManager manager;
    ViewPager viewPager;
    FrameLayout container;
    Week1Fragment week1Fragment;
    Week2Fragment week2Fragment;
    TabAdapter tabAdapter;
    String groupTitle;
    Toolbar toolbar;
    BottomBar bottomBar;
    List<Fragment> fragments;
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
        tabAdapter = new TabAdapter(manager);
        viewPager = findViewById(R.id.view_pager);
        container = findViewById(R.id.contentContainer);
         bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        NYBus.get().register(this);
        super.onResume();
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
        fragments = tabAdapter.selectTab(tabId);
        NYBus.get().post(new ShowScheduleEvent(false, 0, "noName"));
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {

    }
    
    @Subscribe
    public void onGettingScheduleEvent(GettingScheduleEvent event) {

    }
    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event){
    tabAdapter.updateUI(groupTitle, event.isShown(), event.getDayNumber(), toolbar);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event){
        if(event.getError().equals("reload")){
            startActivity(new Intent(this, MainActivity.class).putExtra(getString(R.string.Reload), "reload"));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NYBus.get().unregister(this);
    }

    @Override
    public void onBackPressed() {

        week1Fragment = (Week1Fragment) fragments.get(0);
        week2Fragment = (Week2Fragment) fragments.get(1);
        if(bottomBar.getCurrentTabPosition() == 0) {
            week1Fragment.onBackPressed();
        }else if(bottomBar.getCurrentTabPosition() == 1){
            week2Fragment.onBackPressed();
        }
        else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Натисніть знову для виходу", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }
    }


}
