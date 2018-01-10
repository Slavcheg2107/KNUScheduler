package com.knu.krasn.knuscheduler.Activities;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Facultet;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.FacultyRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.GroupRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.MoveToNextEvent;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.knu.krasn.knuscheduler.Utils.Decor.GridSpacingItemDecoration;
import com.knu.krasn.knuscheduler.View.MainActivityView;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.thread.NYThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;

import static com.knu.krasn.knuscheduler.ApplicationClass.settings;

public class MainActivity extends AppCompatActivity implements MainActivityView {
    FacultyRecyclerAdapter facultyRecyclerAdapter;
    GroupRecyclerAdapter groupRecyclerAdapter;
    RecyclerView recyclerView;
    NetworkService networkService;
    List<Facultet> facultets;
    List<Group> groups;
    TextView mainHeader;
    RelativeLayout loadingWheel;
    ProgressBar pb;
    Realm realm;
    String groupTitle;
    private boolean doubleBackToExitPressedOnce;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.prefs_main, false);
        pb = findViewById(R.id.progressBar);
        pb.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        realm = ApplicationClass.getRealm();
        prefs = ApplicationClass.getPreferences();
        setupView();

        toolbar.setTitle(getString(R.string.choose_fac_title));
        checkCurrentWeek();

    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(2, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
    }

    @Override()
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        NYBus.get().unregister(this);
        networkService.mCompositeDisposable.clear();
    }

    @Subscribe(threadType = NYThread.MAIN)
    public void onGettingGroupsEvent(GettingGroupsEvent event) {
        groups = realm.where(Group.class).findAll();
        groupRecyclerAdapter = new GroupRecyclerAdapter(this, groups, networkService);
        if (loadingWheel.isShown()) {
            loadingWheel.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(groupRecyclerAdapter);
        groupRecyclerAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.search).getActionView();
        setUpSearchView(sv);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpSearchView(SearchView searchView) {
        int searchSrcTextId = android.support.v7.appcompat.R.id.search_src_text;
        EditText searchEditText = searchView.findViewById(searchSrcTextId);
        searchEditText.setText("");
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        LinearLayout searchBar = searchView.findViewById(R.id.search_bar);
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(200);
        searchBar.setLayoutTransition(transition);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        if (event.getError().equals("Кликнулось")) {
            loadingWheel.setVisibility(View.VISIBLE);
        }
        Log.e("ErrorEvent", event.getError());
    }

    @Subscribe
    public void onConnectionEvent(ConnectionEvent event) {
        Toast.makeText(getApplicationContext(), "Немає з'єднання", Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onMoveToNextEvent(MoveToNextEvent event) {
        Intent i = new Intent(this, ScheduleActivity.class);
        i.putExtra(getString(R.string.current_group), event.getMessage());
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NYBus.get().register(this);
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getAdapter() instanceof FacultyRecyclerAdapter) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Натисніть знову для виходу", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else if (recyclerView.getAdapter() instanceof GroupRecyclerAdapter) {
            recyclerView.setAdapter(facultyRecyclerAdapter);
        }
    }

    @Override
    public void setupView() {
        if (getIntent().hasExtra(getString(R.string.Reload))) {
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            prefs.edit().putString(getString(R.string.current_group), "").apply();
        }
        groupTitle = prefs.getString(getString(R.string.current_group), "");
        if (!groupTitle.equals("")) {
            NYBus.get().post(new MoveToNextEvent(groupTitle));
        }
        recyclerView = findViewById(R.id.recycler_view);

        facultets = new ArrayList<>();
        if (realm.where(Facultet.class).findAll().isEmpty()) {
            facultets.add(new Facultet("ФІТ"));
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(facultets);
            realm.commitTransaction();
        } else {
            facultets = realm.where(Facultet.class).findAll();
        }
        networkService = new NetworkService();
        setupRecyclerView();
        if (!getIntent().hasExtra("getGroup")) {
            facultyRecyclerAdapter = new FacultyRecyclerAdapter(this, facultets, networkService);
            recyclerView.setAdapter(facultyRecyclerAdapter);
            facultyRecyclerAdapter.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        } else {
            NYBus.get().post(new GettingGroupsEvent());
        }
        loadingWheel = findViewById(R.id.wheel);
    }

    private void checkCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        int currentWeekOfYear = 0;
        String currentWeek = "";
        try {
            currentWeekOfYear = settings.getInt(getResources().getString(R.string.key_current_week_of_year), -1);
            currentWeek = settings.getString(getResources().getString(R.string.key_current_week), getString(R.string.current_week));
        } catch (Exception e) {
            return;
        }

        List<String> weeks = Arrays.asList(getResources().getStringArray(R.array.week_selection));
        if (currentWeekOfYear != -1) {
            if (currentWeekOfYear < (cal.get(Calendar.WEEK_OF_YEAR)) || currentWeekOfYear == 1) {
                if (currentWeek.equals(weeks.get(0))) {
                    settings.edit().putString(getString(R.string.key_current_week), weeks.get(1)).apply();
                } else if (currentWeek.equals(weeks.get(1))) {
                    settings.edit().putString(getString(R.string.key_current_week), weeks.get(0)).apply();
                }
            }
        }

    }

}



