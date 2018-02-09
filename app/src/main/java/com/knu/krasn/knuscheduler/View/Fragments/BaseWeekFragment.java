package com.knu.krasn.knuscheduler.View.Fragments;

import android.support.v7.widget.RecyclerView;

import com.knu.krasn.knuscheduler.Presenter.Events.ShowScheduleEvent;

/**
 * Created by krasn on 11/20/2017.
 */

public interface BaseWeekFragment {
    RecyclerView.Adapter onBackPressed();

    void setupRecyclerView(ShowScheduleEvent event);

    int checkCurrentDayOfWeek();
    int getCurrentDay();

}
