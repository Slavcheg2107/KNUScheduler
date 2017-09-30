package com.example.krasn.knuscheduler.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.krasn.knuscheduler.Adapters.ScheduleRecyclerAdapter;
import com.example.krasn.knuscheduler.ApplicationClass;
import com.example.krasn.knuscheduler.Decor.GridSpacingItemDecoration;
import com.example.krasn.knuscheduler.Events.GettingScheduleEvent;
import com.example.krasn.knuscheduler.Events.UpdateAdapterEvent;
import com.example.krasn.knuscheduler.Models.GroupModel.Group;
import com.example.krasn.knuscheduler.Models.WeekModel.Week1;
import com.example.krasn.knuscheduler.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;

/**
 * Created by krasn on 9/26/2017.
 */

public class WeekFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerView;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    Realm realm = ApplicationClass.getRealm();
    Group group;
    Week1 week1 = null;
    String groupTitle;
    SharedPreferences prefs = ApplicationClass.getPreferences();
    SharedPreferences.Editor editor = prefs.edit();
    String s = prefs.getString("GroupLoaded","");
    RelativeLayout loadingWheel;
    public WeekFragment(){}
    public static WeekFragment newInstance(Integer number) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, number);
        WeekFragment sampleFragment = new WeekFragment();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week1, container, false);
        EventBus.getDefault().register(this);
        groupTitle = getActivity().getIntent().getStringExtra("group");
        loadingWheel = rootView.findViewById(R.id.wheel2);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        group = realm.where(Group.class).equalTo("title",groupTitle).findFirst();
        if (group != null) {
            week1 = group.getWeek1();
        }
        if (week1 == null) {
            ApplicationClass.getNetwork().getSchedule(groupTitle);
        }
        else {
            scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(getContext(), week1.getSchedules(), ApplicationClass.getNetwork());
            recyclerView.setAdapter(scheduleRecyclerAdapter);
            loadingWheel.setVisibility(View.GONE);
            scheduleRecyclerAdapter.notifyDataSetChanged();
            if(s.equals("")) {
                editor.putString("GroupLoaded", groupTitle);
                editor.apply();
            }
        }

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Subscribe
    public void onGettingSchedulevent(GettingScheduleEvent event) {

        group = realm.where(Group.class).equalTo("title", event.getMessage()).findFirst();
        if (group != null) {
            week1 = group.getWeek1();
        }
        if (week1 == null) {
            ApplicationClass.getNetwork().getSchedule(event.getMessage());
        } else{
            scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(getActivity().getApplicationContext(),
                    week1.getSchedules(), ApplicationClass.getNetwork());
            loadingWheel.setVisibility(View.GONE);
            recyclerView.setAdapter(scheduleRecyclerAdapter);
            scheduleRecyclerAdapter.notifyDataSetChanged();
            if(s.equals("")) {
                editor.putString("GroupLoaded", groupTitle);
                editor.apply();
            }
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
