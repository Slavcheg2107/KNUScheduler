package com.knu.krasn.knuscheduler.View.Activities;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel.Week1;
import com.knu.krasn.knuscheduler.Presenter.Adapters.FragmentController.SchedulePagerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Adapters.FragmentController.ToolbarUpdater;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.Week1RecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.Week2RecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Presenter.Utils.AppRater;
import com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NotificationService;
import com.knu.krasn.knuscheduler.View.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.View.Fragments.Week2Fragment;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;

import java.util.Arrays;
import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;

import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NotificationService.currentGroup;


public class ScheduleActivity extends AppCompatActivity implements MenuItem.OnActionExpandListener {

    FragmentManager manager;
    ViewPager viewPager;
    String groupTitle;
    Toolbar toolbar;

    TabLayout tabLayout;
    SchedulePagerAdapter adapter;
    private String currentWeek;
    private SearchView sv;
    private boolean doubleBackToExitPressedOnce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);
        AppRater.app_launched(this);
        if (getIntent().hasExtra(getString(R.string.current_group))) {
            groupTitle = getIntent().getStringExtra(getString(R.string.current_group));
            settings.edit().putString(getString(R.string.current_group), groupTitle).apply();
        } else {
            groupTitle = settings.getString(getString(R.string.current_group), "");
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(groupTitle);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = findViewById(R.id.tabs);
        manager = getSupportFragmentManager();
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        setupTabLayout(tabLayout);
        showSettingsDialog();
        startNotifications();

    }

    private void setUpSearchView(SearchView searchView) {
        int searchSrcTextId = android.support.v7.appcompat.R.id.search_src_text;
        EditText searchEditText = searchView.findViewById(searchSrcTextId);
        searchEditText.setText("");
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        LinearLayout searchBar = searchView.findViewById(R.id.search_bar);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(200);
        searchBar.setLayoutTransition(transition);

    }


    private void startNotifications() {
        if (isScheduleLoaded(groupTitle)) {
            Intent i = new Intent(this, NotificationService.class);
            i.putExtra(getString(R.string.current_group), groupTitle);
            startService(i);
        }
    }

    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.setLayoutTransition(new LayoutTransition());
        List<String> weekList = Arrays.asList(getResources().getStringArray(R.array.week_selection));
        currentWeek = settings.getString(getString(R.string.key_current_week), getString(R.string.choose_week_title));
        for (int i = 0; i < weekList.size(); i++) {
            if (weekList.get(i).equals(currentWeek)) {
                tabLayout.getTabAt(i).select();
            }
        }
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            linearLayout.getChildAt(i).setRotationX(180f);
        }
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_week1_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_week2_tab);

    }

    private void showSettingsDialog() {
        String s = getString(R.string.choose_week_title);
        currentWeek = settings.getString(getString(R.string.key_current_week), s);
        if (currentWeek.equals(s)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog manageWeekDialog = builder.create();
            manageWeekDialog.setCancelable(false);
            manageWeekDialog.setTitle(getString(R.string.chooseWeek_dialog_title));
            manageWeekDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", ((dialog, which)
                    -> {
                manageWeekDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                finish();
            }));

            manageWeekDialog.show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (NYBus.get().isRegistered(this)) {
            NYBus.get().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        if (!NYBus.get().isRegistered(this)) {
            NYBus.get().register(this);
        }
        currentGroup = groupTitle;
        setupTabLayout(tabLayout);
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        sv = (SearchView) menu.findItem(R.id.search).getActionView();
        setUpSearchView(sv);
        MenuItem item = menu.findItem(R.id.search);
        item.setOnActionExpandListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }
        if (id == R.id.search) {
            startActivity(new Intent(this, SearchActivity.class));
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
            startActivity(new Intent(this, MainActivity.class).putExtra(getString(R.string.key_reload), "reload"));
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
            if (doubleBackToExitPressedOnce) {
                finish();
            } else {
                Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();
            }
            this.doubleBackToExitPressedOnce = true;

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SchedulePagerAdapter(getSupportFragmentManager(), new ToolbarUpdater(groupTitle, toolbar));
        adapter.addFragment(Week1Fragment.newInstance(), getString(R.string.week));
        adapter.addFragment(Week2Fragment.newInstance(), getString(R.string.week));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.addOnPageChangeListener(adapter);
        tabLayout.addOnTabSelectedListener(adapter);
    }

    public static boolean isScheduleLoaded(String groupTitle) {
        try {
            Group group = ApplicationClass.getRealm().where(Group.class).equalTo("title", groupTitle).findFirst();
            Week1 week1 = group.getWeek1();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {


        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {

        return false;
    }
}
