package com.knu.krasn.knuscheduler.View.Fragments;


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
import android.widget.TextView;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel.Week2;
import com.knu.krasn.knuscheduler.Presenter.Adapters.FragmentController.ToolbarUpdater;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.ScheduleRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.Week2RecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Presenter.Utils.Decor.GridSpacingItemDecoration;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;

import java.util.Arrays;
import java.util.Calendar;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;

import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NetworkConnectionChecker.isOnline;

/**
 * Created by krasn on 9/26/2017.
 */

public class Week2WeekFragment extends Fragment implements BaseWeekFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public ToolbarUpdater toolbarUpdater;
    RecyclerView recyclerView;
    Week2RecyclerAdapter week2RecyclerAdapter;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    Realm realm = ApplicationClass.getRealm();

    String groupTitle;
    Week2 week2 = null;
    Group group;
    RelativeLayout loadingWheel;
    TextView emptyData;
    String currentWeek;
    private int dayNumber = 0;

    public Week2WeekFragment() {
    }

    public static Week2WeekFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 2);
        Week2WeekFragment sampleFragment = new Week2WeekFragment();
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
        emptyData = rootView.findViewById(R.id.empty_view);
        groupTitle = settings.getString(getString(R.string.current_group), "");
        toolbarUpdater = new ToolbarUpdater(groupTitle, getActivity().findViewById(R.id.toolbar));
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(ApplicationClass.getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        if (isOnline(ApplicationClass.getContext())) {
            ApplicationClass.getNetwork().getSchedule(groupTitle);

        } else {
            setupRecyclerView(new ShowScheduleEvent(checkCurrentDayOfWeek(), getString(R.string.week2_adapter_name)));
        }
        return rootView;
    }


    @Override
    public void onResume() {
        NYBus.get().register(this);
        super.onResume();
    }


    @Subscribe
    public void onGettingSchedulevent(GettingScheduleEvent event) {
        setupRecyclerView(new ShowScheduleEvent(checkCurrentDayOfWeek(), getString(R.string.week2_adapter_name)));
    }

    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event) {
        setupRecyclerView(event);
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
    public RecyclerView.Adapter onBackPressed() {
        RecyclerView.Adapter currentAdapter = recyclerView.getAdapter();
        if (currentAdapter instanceof ScheduleRecyclerAdapter) {
            recyclerView.setAdapter(week2RecyclerAdapter);
            recyclerView.scheduleLayoutAnimation();
            dayNumber = 0;
            toolbarUpdater.updateToolbar(dayNumber);
        }
        return currentAdapter;
    }

    @Override
    public void setupRecyclerView(ShowScheduleEvent event) {
        group = realm.where(Group.class).equalTo("title", groupTitle).findFirst();
        if (group != null) {
            week2 = group.getWeek2();
        }

        if (week2 == null) {

            if (isOnline(ApplicationClass.getContext())) {
                ApplicationClass.getNetwork().getSchedule(groupTitle);
            } else {
                NYBus.get().post(new ConnectionEvent());
            }
        } else if (event != null && event.getAdapterName().equals(getString(R.string.week2_adapter_name))) {

            week2RecyclerAdapter = new Week2RecyclerAdapter(ApplicationClass.getContext(), week2.getDays());

            dayNumber = event.getDayNumber();
            DayOfWeek dayOfWeek = new DayOfWeek();
            for (DayOfWeek dayOfWeek1 : week2.getDays()) {
                if (dayOfWeek1.getDayNumber() == event.getDayNumber()) {
                    dayOfWeek = dayOfWeek1;
                }
            }
            if (event.getDayNumber() != 0) {
                scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(ApplicationClass.getContext(), dayOfWeek.getScheduleList(), 0);
                recyclerView.setAdapter(scheduleRecyclerAdapter);

                if (!dayOfWeek.getScheduleList().isEmpty()) {
                    loadingWheel.setVisibility(View.GONE);
                    toolbarUpdater.updateToolbar(dayNumber);
                    emptyData.setVisibility(View.GONE);
                    scheduleRecyclerAdapter.notifyDataSetChanged();
                } else {
                    emptyData.setVisibility(View.VISIBLE);
                    loadingWheel.setVisibility(View.GONE);
                }
            } else {
                emptyData.setVisibility(View.GONE);
                loadingWheel.setVisibility(View.GONE);
                recyclerView.setAdapter(week2RecyclerAdapter);
            }

        }
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public int checkCurrentDayOfWeek() {
        currentWeek = settings.getString(getString(R.string.key_current_week), "");
        Integer currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        String s = Arrays.asList(getResources().getStringArray(R.array.week_selection)).get(1);
        if (((currentDayOfWeek > 0) && (currentDayOfWeek < 6)) && (currentWeek.equals(s))) {
            return currentDayOfWeek;
        } else return 0;
    }


    @Override
    public int getCurrentDay() {
        return dayNumber;
    }
}
