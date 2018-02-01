package com.knu.krasn.knuscheduler.View.Fragments;


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

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.WeekModel.Week1;
import com.knu.krasn.knuscheduler.Presenter.Adapters.FragmentController.ToolbarUpdater;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.ScheduleRecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters.Week1RecyclerAdapter;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingScheduleEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.knu.krasn.knuscheduler.Presenter.Utils.Decor.GridSpacingItemDecoration;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;

import java.util.Arrays;
import java.util.Calendar;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;

import static com.knu.krasn.knuscheduler.ApplicationClass.getRealm;
import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NetworkConnectionChecker.isOnline;

/**
 * Created by krasn on 9/26/2017.
 */

public class Week1Fragment extends Fragment implements BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerView;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    Week1RecyclerAdapter week1RecyclerAdapter;
    NetworkService networkService = ApplicationClass.getNetwork();
    Realm realm = getRealm();
    Group group;
    Week1 week1 = null;
    String groupTitle;
    SharedPreferences prefs = ApplicationClass.getPreferences();
    String s;
    RelativeLayout loadingWheel;
    public ToolbarUpdater toolbarUpdater;
    private int dayNumber;
    private String currentWeek;

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
        s = prefs.getString(getString(R.string.current_group), "");
        ProgressBar pb = rootView.findViewById(R.id.progressBar);
        pb.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        groupTitle = settings.getString(getString(R.string.current_group), "");
        toolbarUpdater = new ToolbarUpdater(groupTitle, getActivity().findViewById(R.id.toolbar));
        loadingWheel = rootView.findViewById(R.id.wheel2);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 1));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(1, 10, false, getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        setRecyclerAdapter(new ShowScheduleEvent(checkCurrentDayOfWeek(), getString(R.string.week1_adapter_name)));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        NYBus.get().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        NYBus.get().unregister(this);
    }

    @Subscribe
    public void onGettingSchedulevent(GettingScheduleEvent event) {
        setRecyclerAdapter(new ShowScheduleEvent(checkCurrentDayOfWeek(), getString(R.string.week1_adapter_name)));
    }


    @Subscribe
    public void onShowScheduleEvent(ShowScheduleEvent event) {
        setRecyclerAdapter(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void setRecyclerAdapter(ShowScheduleEvent event) {
        group = realm.where(Group.class).equalTo("title", groupTitle).findFirst();
        if (group != null) {
            week1 = group.getWeek1();
        }
        if (week1 == null) {

            if (isOnline(ApplicationClass.getContext())) {
                ApplicationClass.getNetwork().getSchedule(groupTitle);
            } else {
                NYBus.get().post(new ConnectionEvent());
            }
        } else if (event != null && event.getAdapterName().equals(getString(R.string.week1_adapter_name))) {
            week1RecyclerAdapter = new Week1RecyclerAdapter(ApplicationClass.getContext(),
                    week1.getDays(), networkService);

            dayNumber = event.getDayNumber();
            DayOfWeek dayOfWeek = new DayOfWeek();
            for (DayOfWeek dayOfWeek1 : week1.getDays()) {
                if (dayOfWeek1.getDayNumber() == event.getDayNumber()) {
                    dayOfWeek = dayOfWeek1;
                }
            }
            if (event.getDayNumber() != 0) {
                loadingWheel.setVisibility(View.GONE);
                scheduleRecyclerAdapter = new ScheduleRecyclerAdapter(ApplicationClass.getContext(),
                        dayOfWeek.getScheduleList(), networkService);
                recyclerView.setAdapter(scheduleRecyclerAdapter);
                toolbarUpdater.updateToolbar(dayNumber);
                scheduleRecyclerAdapter.notifyDataSetChanged();
            } else {
                loadingWheel.setVisibility(View.GONE);
                recyclerView.setAdapter(week1RecyclerAdapter);
            }
        }
        recyclerView.scheduleLayoutAnimation();
    }

    public int checkCurrentDayOfWeek() {
        currentWeek = settings.getString(getString(R.string.key_current_week), "");
        Integer currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        int d = currentDayOfWeek;
        String s = Arrays.asList(getResources().getStringArray(R.array.week_selection)).get(0);
        if (((currentDayOfWeek > 0) && (currentDayOfWeek < 6)) && (currentWeek.equals(s))) {
            return currentDayOfWeek;
        } else return 0;
    }

    @Override
    public RecyclerView.Adapter onBackPressed() {
        RecyclerView.Adapter currentAdapter = recyclerView.getAdapter();
        if (currentAdapter instanceof ScheduleRecyclerAdapter) {
            recyclerView.setAdapter(week1RecyclerAdapter);
            recyclerView.scheduleLayoutAnimation();
            dayNumber = 0;
            toolbarUpdater.updateToolbar(dayNumber);
        }
        return currentAdapter;
    }

    @Override
    public int getCurrentDay() {
        return dayNumber;
    }
}
