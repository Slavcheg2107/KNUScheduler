package com.knu.krasn.knuscheduler.View.Activities;

import android.animation.LayoutTransition;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties.Faculty;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.FacultyRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.GroupRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GetFacultiesEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.MoveToNextEvent;
import com.knu.krasn.knuscheduler.Presenter.Listeners.RxSearchObservable;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.knu.krasn.knuscheduler.Presenter.Utils.Decor.GridSpacingItemDecoration;
import com.knu.krasn.knuscheduler.View.MainActivityView;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.thread.NYThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import geek.owl.com.ua.KNUSchedule.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.knu.krasn.knuscheduler.ApplicationClass.getContext;
import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NetworkConnectionChecker.isOnline;

public class MainActivity extends AppCompatActivity implements MainActivityView, MenuItem.OnActionExpandListener {

    FacultyRecyclerAdapter facultyRecyclerAdapter;
    GroupRecyclerAdapter groupRecyclerAdapter;
    RecyclerView recyclerView;
    NetworkService networkService;
    List<Faculty> faculties;
    List<Group> groups;
    int resultCode;
    RelativeLayout loadingWheel;
    ProgressBar pb;
    Realm realm;
    String groupTitle;
    private boolean doubleBackToExitPressedOnce;
    RealmResults<Group> groupRealmResults;
    SearchView sv;
    private RealmResults<Faculty> facultyRealmResults;
    private GroupRecyclerAdapter searchGroupAdapter;
    private FacultyRecyclerAdapter searchFacultyAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingWheel = findViewById(R.id.wheel);
        loadingWheel.setVisibility(View.VISIBLE);
        resultCode = 0;
        networkService = new NetworkService();
        setupRecyclerView();
        PreferenceManager.setDefaultValues(this, R.xml.prefs_main, false);
        pb = findViewById(R.id.progressBar);
        pb.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        realm = ApplicationClass.getRealm();
        groupTitle = settings.getString(getString(R.string.current_group), "");
        toolbar.setTitle(getString(R.string.app_name));

        checkCurrentWeek();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
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
    }

    @Subscribe(threadType = NYThread.MAIN)
    public void onGettingGroupsEvent(GettingGroupsEvent event) {
        groups = event.getGroups();
        groupRecyclerAdapter = new GroupRecyclerAdapter(this, groups, networkService);
        if (loadingWheel.isShown()) {
            loadingWheel.setVisibility(View.GONE);
        }
        toolbar.setTitle(getString(R.string.choose_group));
        recyclerView.setAdapter(groupRecyclerAdapter);
        groupRecyclerAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);
        settings.setEnabled(false);
        sv = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem search = menu.findItem(R.id.search);

        setUpSearchView(sv);
        search.setOnActionExpandListener(this);
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
        searchView.setMaxWidth(Integer.MAX_VALUE);
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(200);
        searchBar.setLayoutTransition(transition);

    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        if (event.getError().equals("Кликнулось")) {
            loadingWheel.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onConnectionEvent(ConnectionEvent event) {
        Toast.makeText(getApplicationContext(), R.string.no_connetction, Toast.LENGTH_LONG).show();
        if (loadingWheel.getVisibility() == View.VISIBLE) {
            loadingWheel.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onMoveToNextEvent(MoveToNextEvent event) {
        if (!realm.where(Group.class).contains("title", event.getMessage()).findAll().isEmpty()) {
            Intent i = new Intent(this, ScheduleActivity.class);
            i.putExtra(getString(R.string.current_group), event.getMessage());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            groupTitle = event.getMessage();
            startActivity(i);
            finish();
        } else {
            settings.edit().putString(getString(R.string.current_group), "").apply();
            setupView();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NYBus.get().isRegistered(MainActivity.class)) {
            NYBus.get().register(this);
        }
        setupView();


    }


    @Override
    public void onBackPressed() {
        if (recyclerView.getAdapter() instanceof FacultyRecyclerAdapter) {
            if (doubleBackToExitPressedOnce) {
                finish();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.press_again), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else if (recyclerView.getAdapter() instanceof GroupRecyclerAdapter) {
            if (sv.isIconified()) {
                recyclerView.setAdapter(facultyRecyclerAdapter);
                toolbar.setTitle(getString(R.string.choose_fac_title));
            }
        }
    }

    @Override
    public void setupView() {

        if (getIntent().hasExtra(getString(R.string.key_reload))) {
            realm = ApplicationClass.getRealm();
            if (!realm.isInTransaction())
                realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            settings.edit().putString(getString(R.string.current_group), "").apply();
            if (isOnline(getContext())) {
                networkService.getFaculties();
                getIntent().removeExtra(getString(R.string.key_reload));
            } else {
                Toast.makeText(this, getString(R.string.no_connetction), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        groupTitle = settings.getString(getString(R.string.current_group), "");

        if (!groupTitle.equals("")) {
            if (resultCode != -1) {
                if (!NYBus.get().isRegistered(MainActivity.class)) {
                    NYBus.get().register(this);
                }
                NYBus.get().post(new MoveToNextEvent(groupTitle));
            }
        }

        faculties = new ArrayList<>();
        if (realm.where(Faculty.class).findAll().isEmpty()) {
            networkService.getFaculties();
            loadingWheel.setVisibility(View.VISIBLE);
            return;
        } else {
            faculties = realm.where(Faculty.class).findAll();
            loadingWheel.setVisibility(View.GONE);


            if (!getIntent().hasExtra(getString(R.string.key_get_group))) {
                facultyRecyclerAdapter = new FacultyRecyclerAdapter(this, faculties, networkService);
                if (loadingWheel.getVisibility() == View.VISIBLE) {
                    loadingWheel.setVisibility(View.GONE);
                }
                toolbar.setTitle(getString(R.string.choose_fac_title));
            recyclerView.setAdapter(facultyRecyclerAdapter);
            facultyRecyclerAdapter.notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
        }
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NYBus.get().unregister(this);
    }

    @Subscribe(threadType = NYThread.MAIN)
    public void onGettingFacultiesEvent(GetFacultiesEvent event) {
        setupView();
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

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        RxSearchObservable.fromView(sv).debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe(new Observer<String>() {
                    private RecyclerView.Adapter currentAdapter;
                    @Override
                    public void onSubscribe(Disposable d) {

                        searchGroupAdapter = new GroupRecyclerAdapter(getApplicationContext(), new ArrayList<Group>(), networkService);
                        searchFacultyAdapter = new FacultyRecyclerAdapter(getApplicationContext(), new ArrayList<Faculty>(), networkService);
                    }

                    @Override
                    public void onNext(String s) {
                        currentAdapter = recyclerView.getAdapter();
                        if (currentAdapter instanceof GroupRecyclerAdapter) {
                            recyclerView.setAdapter(searchGroupAdapter);
                            if (!s.isEmpty()) {
                                groupRealmResults = realm.where(Group.class).contains("title", s.toUpperCase()).findAll();
                                searchGroupAdapter.updateData(groupRealmResults);

                            } else {
                                recyclerView.setAdapter(groupRecyclerAdapter);

                            }
                        }
                        if (currentAdapter instanceof FacultyRecyclerAdapter) {
                            recyclerView.setAdapter(searchFacultyAdapter);
                            if (!s.isEmpty()) {
                                facultyRealmResults = realm.where(Faculty.class).contains("name", s.toUpperCase()).findAll();
                                searchFacultyAdapter.updateData(facultyRealmResults);
                            } else {
                                recyclerView.setAdapter(facultyRecyclerAdapter);

                            }
                        }
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        if (recyclerView.getAdapter() instanceof FacultyRecyclerAdapter) {
            recyclerView.setAdapter(facultyRecyclerAdapter);
        } else {
            recyclerView.setAdapter(groupRecyclerAdapter);
        }
        return true;
    }
}



