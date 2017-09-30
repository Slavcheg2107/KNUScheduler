package com.example.krasn.knuscheduler.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krasn.knuscheduler.Adapters.ScheduleRecyclerAdapter;
import com.example.krasn.knuscheduler.ApplicationClass;
import com.example.krasn.knuscheduler.Decor.GridSpacingItemDecoration;
import com.example.krasn.knuscheduler.Events.ConnectionEvent;
import com.example.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.example.krasn.knuscheduler.Events.UpdateAdapterEvent;
import com.example.krasn.knuscheduler.Models.GroupModel.Group;
import com.example.krasn.knuscheduler.Models.WeekModel.Week2;
import com.example.krasn.knuscheduler.Network.NetworkConnection;
import com.example.krasn.knuscheduler.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;

/**
 * Created by krasn on 9/26/2017.
 */

public class Week2Fragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerView;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    Realm realm = ApplicationClass.getRealm();
    String groupTitle;
    Week2 week2 = null;
    Group group;
    public Week2Fragment(){}


    public static Week2Fragment newInstance(Integer number) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, number);

        Week2Fragment sampleFragment = new Week2Fragment();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_week2, container, false);
        EventBus.getDefault().register(this);
        groupTitle =  getActivity().getIntent().getStringExtra("group");
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        rootView.findViewById(R.id.wheel2).setVisibility(View.GONE);

        group = realm.where(Group.class).equalTo("title",groupTitle).findFirst();
        if (group != null) {
            week2 = group.getWeek2();
        }
        if (week2 == null) {
            NetworkConnection nc = new NetworkConnection(ApplicationClass.getContext());
            if(nc.isOnline()) {
                ApplicationClass.getNetwork().getSchedule(groupTitle);
            }else{
                EventBus.getDefault().post(new ConnectionEvent());
            }

        }else {
            scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(getContext(), week2.getSchedules(), ApplicationClass.getNetwork());
            recyclerView.setAdapter(scheduleRecyclerAdapter);
            scheduleRecyclerAdapter.notifyDataSetChanged();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        } else{
                scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(getActivity().getApplicationContext(),
                        week2.getSchedules(), ApplicationClass.getNetwork());

            recyclerView.setAdapter(scheduleRecyclerAdapter);
            scheduleRecyclerAdapter.notifyDataSetChanged();
        }
    }
    @Subscribe
    public void onUpdateAdapter(UpdateAdapterEvent event){
        scheduleRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
