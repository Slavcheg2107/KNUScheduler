package com.example.krasn.knuscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.example.krasn.knuscheduler.Events.UpdateAdapterEvent;
import com.example.krasn.knuscheduler.Fragments.Week2Fragment;
import com.example.krasn.knuscheduler.Fragments.WeekFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ScheduleActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    FragmentTransaction ft;
    FragmentManager manager;
    ViewPager viewPager;
    FrameLayout container;
    WeekFragment week1Fragment;
    Week2Fragment week2Fragment;
    String week1Tag = "week1";
    String week2Tag = "week2";
    String groupTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_main2);

        groupTitle = getIntent().getStringExtra("group");
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(groupTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        container = (FrameLayout) findViewById(R.id.contentContainer);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

            week1Fragment = (WeekFragment) getSupportFragmentManager().findFragmentByTag(week1Tag);
            if (week1Fragment == null) {
                week1Fragment = WeekFragment.newInstance(1);
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

        EventBus.getDefault().post(new UpdateAdapterEvent());
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {

    }

    @Subscribe
    public void onGettingScheduleEvent(GettingScheduleEvent event) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
