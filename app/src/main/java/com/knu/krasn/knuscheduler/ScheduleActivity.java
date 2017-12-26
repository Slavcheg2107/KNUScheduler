package com.knu.krasn.knuscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.knu.krasn.knuscheduler.Adapters.FragmentController.SchedulePagerAdapter;
import com.knu.krasn.knuscheduler.Adapters.FragmentController.ToolbarUpdater;
import com.knu.krasn.knuscheduler.Adapters.RecyclerViewAdapters.Week1RecyclerAdapter;
import com.knu.krasn.knuscheduler.Adapters.RecyclerViewAdapters.Week2RecyclerAdapter;
import com.knu.krasn.knuscheduler.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.Fragments.Week2Fragment;
import com.knu.krasn.knuscheduler.Models.GroupModel.Group;
import com.knu.krasn.knuscheduler.Models.WeekModel.Week1;
import com.knu.krasn.knuscheduler.ServiceUtils.NotificationService;
import com.knu.krasn.knuscheduler.Utils.AppRater;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;

import geek.owl.com.ua.KNUSchedule.R;

import static com.knu.krasn.knuscheduler.ServiceUtils.NotificationService.currentGroup;


public class ScheduleActivity extends AppCompatActivity {

    FragmentManager manager;
    ViewPager viewPager;
    ToolbarUpdater toolbarUpdater;
    String groupTitle;
    Toolbar toolbar;

    TabLayout tabLayout;
    SchedulePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);
        AppRater.app_launched(this);
        groupTitle = getIntent().getStringExtra(getString(R.string.current_group));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(groupTitle);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabs);
        manager = getSupportFragmentManager();
        toolbarUpdater = new ToolbarUpdater(groupTitle, toolbar);
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager, true);
        setupIcons();
        showSettingsDialog();
        if (isScheduleLoaded(groupTitle)) {
            Intent i = new Intent(this, NotificationService.class);
            i.putExtra(getString(R.string.current_group), groupTitle);
            startService(i);
        }

        Log.e("TAG", "onCreate");

    }

    private void setupIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_week1_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_week2_tab);
    }

    private void showSettingsDialog() {
        String currentWeek = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.key_current_week), getString(R.string.choose_week_title));
        String s = getString(R.string.choose_week_title);
        if (currentWeek.equals(s)) {
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
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        NYBus.get().register(this);
        currentGroup = groupTitle;
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

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

        }
        return super.onOptionsItemSelected(item);
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
        Fragment fragment = this.adapter.getItem(viewPager.getCurrentItem());
        if (fragment instanceof Week1Fragment) {
            adapter = ((Week1Fragment) fragment).onBackPressed();
        } else if (fragment instanceof Week2Fragment) {
            adapter = ((Week2Fragment) fragment).onBackPressed();
        }
        if (adapter != null && (adapter instanceof Week1RecyclerAdapter || adapter instanceof Week2RecyclerAdapter)) {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SchedulePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Week1Fragment.newInstance(), getString(R.string.week));
        adapter.addFragment(Week2Fragment.newInstance(), getString(R.string.week));
        viewPager.setAdapter(adapter);
    }

    public boolean isScheduleLoaded(String groupTitle) {
        try {
            Group group = ApplicationClass.getRealm().where(Group.class).equalTo("title", groupTitle).findFirst();
            Week1 week1 = group.getWeek1();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
