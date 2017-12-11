package com.knu.krasn.knuscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.knu.krasn.knuscheduler.Adapters.FragmentController.TabController;
import com.knu.krasn.knuscheduler.Adapters.RecyclerViewAdapters.Week1RecyclerAdapter;
import com.knu.krasn.knuscheduler.Adapters.RecyclerViewAdapters.Week2RecyclerAdapter;
import com.knu.krasn.knuscheduler.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Fragments.BaseFragment;
import com.knu.krasn.knuscheduler.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.Fragments.Week2Fragment;
import com.knu.krasn.knuscheduler.Utils.AppRater;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;


public class ScheduleActivity extends AppCompatActivity implements OnTabSelectListener {

    FragmentManager manager;
    ViewPager viewPager;
    FrameLayout container;
    Week1Fragment week1Fragment;
    Week2Fragment week2Fragment;
    TabController tabController;
    String groupTitle;
    Toolbar toolbar;
    BottomBar bottomBar;
    List<BaseFragment> fragments;
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
        tabController = new TabController(groupTitle, toolbar);
        viewPager = findViewById(R.id.view_pager);
        container = findViewById(R.id.contentContainer);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);
//        int currentWeek = ApplicationClass.getPreferences().getInt(Settings.CURRENT_WEEK, 0);
//        if (currentWeek != 0) {
//            bottomBar.selectTabAtPosition(currentWeek - 1, true);
//        } else {
//            showSettingsDialog();
//        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog manageWeekDialog = builder.create();
        manageWeekDialog.setCancelable(false);
        manageWeekDialog.setTitle("Виберіть поточний тиждень, будьласка");
        manageWeekDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", ((dialog, which)
                -> {
            manageWeekDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }));
        manageWeekDialog.show();
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

        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
//            SharedPreferences preferences = ApplicationClass.getPreferences();
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("GroupLoaded", "");
//            editor.apply();
//            String s = "getGroup";
//            Intent i = new Intent(this, MainActivity.class);
//            i.putExtra(s, s);
//            startActivity(i);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        fragments = tabController.selectTab(tabId, manager);
    }


    @Subscribe
    public void onGettingScheduleEvent(GettingScheduleEvent event) {

    }

    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event) {

    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        if (event.getError().equals("reload")) {
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
        RecyclerView.Adapter adapter = null;
        week1Fragment = (Week1Fragment) fragments.get(0);
        week2Fragment = (Week2Fragment) fragments.get(1);
        if (bottomBar.getCurrentTabPosition() == 0) {
            adapter = week1Fragment.onBackPressed();
        } else if (bottomBar.getCurrentTabPosition() == 1) {
            adapter = week2Fragment.onBackPressed();
        }
        if (adapter != null && (adapter instanceof Week1RecyclerAdapter || adapter instanceof Week2RecyclerAdapter)) {
            super.onBackPressed();
        }
    }
}
