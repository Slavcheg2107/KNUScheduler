package com.knu.krasn.knuscheduler.View.Activities;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.ScheduleRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Events.SearchSuccesEvent;
import com.knu.krasn.knuscheduler.Presenter.Listeners.RxSearchObservable;
import com.knu.krasn.knuscheduler.Presenter.Listeners.ScrollListener;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.knu.krasn.knuscheduler.Presenter.Utils.Decor.GridSpacingItemDecoration;
import com.knu.krasn.knuscheduler.View.SearchViewActivity;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.thread.NYThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import geek.owl.com.ua.KNUSchedule.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.knu.krasn.knuscheduler.ApplicationClass.getContext;
import static com.knu.krasn.knuscheduler.ApplicationClass.getNetwork;

/**
 * Created by krasn on 1/16/2018.
 */

public class SearchActivity extends AppCompatActivity implements MenuItem.OnActionExpandListener, SearchViewActivity {
    public static RelativeLayout loadingWheel;
    int offset = 0;
    int limit = 6;
    private RecyclerView rv;
    private SearchView sv;
    private NetworkService networkService;
    private ScheduleRecyclerAdapter recyclerAdapter;
    private EditText searchEditText;
    private TextView emptyData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        emptyData = findViewById(R.id.empty_view);
        loadingWheel = findViewById(R.id.wheel);
        networkService = getNetwork();
        setupRecyclerView();

    }


    @Override
    public void showLoader() {
        loadingWheel.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideLoader() {
        if (loadingWheel.getVisibility() == View.VISIBLE) {
            loadingWheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupRecyclerView() {
        recyclerAdapter = new ScheduleRecyclerAdapter(getContext(), new ArrayList<>(), 1);
        rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        rv.getLayoutManager().setAutoMeasureEnabled(true);
        rv.setAdapter(recyclerAdapter);
    }

    @Override
    public void setListenersOnSearchView(SearchView searchView) {
        ScrollListener.on(rv)
                .flatMapSingle(integer -> networkService.getSearchQuery(searchEditText.getText().toString(), limit, offset + limit))
                .subscribe(new Observer<List<Schedule>>() {

                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(List<Schedule> schedules) {
                                   NYBus.get().post(new SearchSuccesEvent(schedules, 1));
                                   hideLoader();
                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );

        RxSearchObservable.fromView(sv)
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .filter(yourString -> !yourString.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMapSingle(s -> networkService.getSearchQuery(s, limit, offset))
                .subscribe(new Observer<List<Schedule>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Schedule> schedules) {
                        NYBus.get().post(new SearchSuccesEvent(schedules, 0));
                        offset = 0;
                        hideLoader();
                    }

                    @Override
                    public void onError(Throwable e) {
                        String s = searchEditText.getText().toString();
                        networkService.getSearchQuery(s, limit, offset);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setupSearchView(SearchView searchView) {
        int searchSrcTextId = android.support.v7.appcompat.R.id.search_src_text;
        searchEditText = searchView.findViewById(searchSrcTextId);
        searchEditText.setText("");
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        LinearLayout searchBar = searchView.findViewById(R.id.search_bar);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(200);
        searchBar.setLayoutTransition(transition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);
        sv = (SearchView) menu.findItem(R.id.search).getActionView();
        setupSearchView(sv);
        MenuItem item = menu.findItem(R.id.search);
        item.setOnActionExpandListener(this);
        item.expandActionView();
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        NYBus.get().register(this);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        sv.requestFocus();
        setListenersOnSearchView(sv);
        return true;
    }

    @Subscribe(threadType = NYThread.MAIN)
    public void onSearchSuccesEvent(SearchSuccesEvent event) {
        List<Schedule> list = event.getSchedules();
        if (event.whatToDo() == 0) {
            if (!event.getSchedules().isEmpty()) {
                emptyData.setVisibility(View.GONE);
                recyclerAdapter.updateData(list);

            } else {
                emptyData.setVisibility(View.VISIBLE);
                recyclerAdapter.updateData(new ArrayList<>());
            }
            rv.scheduleLayoutAnimation();
        } else if (event.whatToDo() == 1) {
            if (!list.isEmpty()) {
                    offset = offset + limit + 1;
                recyclerAdapter.addData(list);
            }
        }
        loadingWheel.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        onBackPressed();
        return true;
    }
}
