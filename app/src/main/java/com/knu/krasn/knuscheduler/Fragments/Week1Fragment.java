package com.knu.krasn.knuscheduler.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.knu.krasn.knuscheduler.Adapters.FragmentController.TabController;
import com.knu.krasn.knuscheduler.Adapters.RecyclerViewAdapters.ScheduleRecyclerAdapter;
import com.knu.krasn.knuscheduler.Adapters.RecyclerViewAdapters.Week1RecyclerAdapter;
import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Decor.GridSpacingItemDecoration;
import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Models.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Models.GroupModel.Group;
import com.knu.krasn.knuscheduler.Models.WeekModel.Week1;
import com.knu.krasn.knuscheduler.Network.NetworkService;
import com.knu.krasn.knuscheduler.ServiceUtils.Notification;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;

/**
 * Created by krasn on 9/26/2017.
 */

public class Week1Fragment extends Fragment implements BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerView;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    Week1RecyclerAdapter week1RecyclerAdapter;
    NetworkService networkService = ApplicationClass.getNetwork();
    Realm realm = ApplicationClass.getRealm();
    Group group;
    Week1 week1 = null;
    String groupTitle;
    SharedPreferences prefs = ApplicationClass.getPreferences();
    SharedPreferences.Editor editor = prefs.edit();
    String s = prefs.getString("GroupLoaded", "");
    RelativeLayout loadingWheel;
    private TabController tabController;
    private int dayNumber;

    public Week1Fragment() {
    }

    public static Week1Fragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 1);
        Week1Fragment sampleFragment = new Week1Fragment();
        sampleFragment.setRetainInstance(true);
        sampleFragment.setArguments(args);

        return sampleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week1, container, false);

        ProgressBar pb = rootView.findViewById(R.id.progressBar);
        pb.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        groupTitle = getActivity().getIntent().getStringExtra("group");
        tabController = new TabController(groupTitle, getActivity().findViewById(R.id.toolbar));
        loadingWheel = rootView.findViewById(R.id.wheel2);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 1));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        group = realm.where(Group.class).equalTo("title", groupTitle).findFirst();
        if (group != null) {
            week1 = group.getWeek1();
        }
        if (week1 == null) {
            ApplicationClass.getNetwork().getSchedule(groupTitle);
        } else {
            week1RecyclerAdapter = new Week1RecyclerAdapter(getContext(), week1.getDays(), networkService);
            loadingWheel.setVisibility(View.GONE);
            recyclerView.setAdapter(week1RecyclerAdapter);
            week1RecyclerAdapter.notifyDataSetChanged();
            if (s.equals("")) {
                editor.putString("GroupLoaded", groupTitle);
                editor.apply();
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        NYBus.get().register(this);
    }

    @Subscribe
    public void onGettingSchedulevent(GettingScheduleEvent event) {
        group = realm.where(Group.class).equalTo("title", event.getMessage()).findFirst();
        if (group != null) {
            week1 = group.getWeek1();
        }
        if (week1 == null) {
            ApplicationClass.getNetwork().getSchedule(event.getMessage());
        } else {
            week1RecyclerAdapter = new Week1RecyclerAdapter(ApplicationClass.getContext(), week1.getDays(), networkService);
            loadingWheel.setVisibility(View.GONE);
            recyclerView.setAdapter(week1RecyclerAdapter);
            recyclerView.scheduleLayoutAnimation();
            week1RecyclerAdapter.notifyDataSetChanged();
            if (s.equals("")) {
                editor.putString("GroupLoaded", groupTitle);
                editor.apply();
            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (recyclerView.getAdapter() instanceof Week1RecyclerAdapter) {
                dayNumber = 0;
            }
            tabController.updateUI(dayNumber);
            recyclerView.scheduleLayoutAnimation();
        }
    }


    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event) {

        if (event.getAdapterName().equals(getString(R.string.week1_adapter_name))) {
            dayNumber = event.getDayNumber();
            DayOfWeek dayOfWeek = new DayOfWeek();
            for (DayOfWeek dayOfWeek1 : week1.getDays()) {
                if (dayOfWeek1.getDayNumber() == event.getDayNumber()) {
                    dayOfWeek = dayOfWeek1;
                }
            }
            if (event.getDayNumber() != 0) {
                scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(ApplicationClass.getContext(), dayOfWeek.getScheduleList(), networkService);
                recyclerView.setAdapter(scheduleRecyclerAdapter);
                scheduleRecyclerAdapter.notifyDataSetChanged();
                Intent notificationService = new Intent(getContext(), Notification.class);
//                notificationService.putExtra(group.getWeek1())
            } else {
                week1RecyclerAdapter = new Week1RecyclerAdapter(ApplicationClass.getContext(), week1.getDays(), networkService);
                recyclerView.setAdapter(week1RecyclerAdapter);
//                week1RecyclerAdapter.notifyDataSetChanged();
            }
            recyclerView.scheduleLayoutAnimation();
            tabController.updateUI(dayNumber);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public RecyclerView.Adapter onBackPressed() {
        RecyclerView.Adapter currentAdapter = recyclerView.getAdapter();
        if (currentAdapter instanceof ScheduleRecyclerAdapter) {
            recyclerView.setAdapter(week1RecyclerAdapter);
            recyclerView.scheduleLayoutAnimation();
            dayNumber = 0;
            tabController.updateUI(dayNumber);
        }
        return currentAdapter;
    }
}
