package com.knu.krasn.knuscheduler.Fragments;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.knu.krasn.knuscheduler.Adapters.ScheduleRecyclerAdapter;
import com.knu.krasn.knuscheduler.Adapters.TabAdapter;
import com.knu.krasn.knuscheduler.Adapters.Week2RecyclerAdapter;
import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Decor.GridSpacingItemDecoration;
import com.knu.krasn.knuscheduler.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Models.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Models.GroupModel.Group;
import com.knu.krasn.knuscheduler.Models.WeekModel.Week2;
import com.knu.krasn.knuscheduler.Network.NetworkService;
import com.knu.krasn.knuscheduler.Utils.NetworkConnection;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;

/**
 * Created by krasn on 9/26/2017.
 */

public class Week2Fragment extends Fragment implements BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerView;
    Week2RecyclerAdapter week2RecyclerAdapter;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    Realm realm = ApplicationClass.getRealm();
    NetworkService networkService = ApplicationClass.getNetwork();
    String groupTitle;
    Week2 week2 = null;
    Group group;
    RelativeLayout loadingWheel;
    private TabAdapter tabAdapter;
    private int dayNumber = 0;

    public Week2Fragment() {
    }


    public static Week2Fragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 2);

        Week2Fragment sampleFragment = new Week2Fragment();
        sampleFragment.setRetainInstance(true);
        sampleFragment.setArguments(args);

        return sampleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_week2, container, false);

        ProgressBar pb = rootView.findViewById(R.id.progressBar);
        pb.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        loadingWheel = rootView.findViewById(R.id.wheel2);
        groupTitle = getActivity().getIntent().getStringExtra("group");
        tabAdapter = new TabAdapter(groupTitle, getActivity().findViewById(R.id.toolbar));
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(ApplicationClass.getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);

        group = realm.where(Group.class).equalTo("title", groupTitle).findFirst();
        if (group != null) {
            week2 = group.getWeek2();
        }
        if (week2 == null) {
            NetworkConnection nc = new NetworkConnection(ApplicationClass.getContext());
            if (nc.isOnline()) {
                ApplicationClass.getNetwork().getSchedule(groupTitle);
            } else {
                NYBus.get().post(new ConnectionEvent());
            }
        } else {
            loadingWheel.setVisibility(View.GONE);
            week2RecyclerAdapter = new Week2RecyclerAdapter(ApplicationClass.getContext(), week2.getDays(), networkService);
            recyclerView.setAdapter(week2RecyclerAdapter);
            week2RecyclerAdapter.notifyDataSetChanged();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        NYBus.get().register(this);
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (recyclerView.getAdapter() instanceof Week2RecyclerAdapter) {
                dayNumber = 0;
            }
            tabAdapter.updateUI(dayNumber);
        }
    }

    @Subscribe
    public void onGettingSchedulevent(GettingScheduleEvent event) {
        Week2 week2 = null;
        Group group = realm.where(Group.class).equalTo("title", event.getMessage()).findFirst();
        if (group != null) {
            week2 = group.getWeek2();
        }
        if (week2 == null) {
            ApplicationClass.getNetwork().getSchedule(event.getMessage());
        } else {
            loadingWheel.setVisibility(View.GONE);
            week2RecyclerAdapter = new Week2RecyclerAdapter(ApplicationClass.getContext(),
                    week2.getDays(), networkService);
            recyclerView.setAdapter(week2RecyclerAdapter);
            week2RecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event) {
        dayNumber = event.getDayNumber();
        if (event.getAdapterName().equals(getString(R.string.week2_adapter_name))) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            for (DayOfWeek dayOfWeek1 : week2.getDays()) {
                if (dayOfWeek1.getDayNumber() == event.getDayNumber()) {
                    dayOfWeek = dayOfWeek1;
                }
            }
            if (event.getDayNumber() != 0) {
                scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(ApplicationClass.getContext(),
                        dayOfWeek.getScheduleList(), networkService);
                recyclerView.setAdapter(scheduleRecyclerAdapter);
                scheduleRecyclerAdapter.notifyDataSetChanged();
            } else {
                week2RecyclerAdapter = new Week2RecyclerAdapter(ApplicationClass.getContext(),
                        week2.getDays(), networkService);
                recyclerView.setAdapter(week2RecyclerAdapter);
                week2RecyclerAdapter.notifyDataSetChanged();
            }
        }
        tabAdapter.updateUI(dayNumber);
    }

    @Override
    public void onPause() {
        NYBus.get().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getAdapter() instanceof ScheduleRecyclerAdapter) {
            recyclerView.setAdapter(week2RecyclerAdapter);
            dayNumber = 0;
            tabAdapter.updateUI(dayNumber);
        }
    }
}
