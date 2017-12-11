package com.knu.krasn.knuscheduler.Adapters.FragmentController;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Fragments.BaseFragment;
import com.knu.krasn.knuscheduler.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.Fragments.Week2Fragment;

import java.util.ArrayList;
import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;

/**
 * Created by krasn on 11/20/2017.
 */

public class TabController {
    private String groupTitle;
    private Toolbar toolbar;
    private Week1Fragment week1Fragment = Week1Fragment.newInstance();
    private Week2Fragment week2Fragment = Week2Fragment.newInstance();
    private Context context = ApplicationClass.getContext();
    private List<BaseFragment> fragments = new ArrayList<>();


    public TabController(String groupTitle, Toolbar toolbar) {
        this.toolbar = toolbar;
        this.groupTitle = groupTitle;
    }

    public List<BaseFragment> selectTab(int tabId, FragmentManager fm) {
//        int currentWeek = ApplicationClass.getPreferences().getInt(Settings.CURRENT_WEEK, 0);
//        if(currentWeek!=0)
        FragmentTransaction ft = fm.beginTransaction();
        int contentContainer = R.id.contentContainer;
        if (fm.getFragments().size() == 0) {
            ft.add(contentContainer, week1Fragment, context.getString(R.string.week1_adapter_name));
            ft.add(contentContainer, week2Fragment, context.getString(R.string.week2_adapter_name));
            fragments.add(week1Fragment);
            fragments.add(week2Fragment);
            ft.hide(week2Fragment);
            ft.commit();
        } else {
            if (tabId == R.id.tab_week1) {
                ft.hide(week2Fragment);
                ft.show(week1Fragment);
                ft.commit();
            } else if (tabId == R.id.tab_week2) {
                ft.hide(week1Fragment);
                ft.show(week2Fragment);
                ft.commit();
            }
        }
        return fragments;
    }


    public void updateUI(int dayNumber) {
        switch (dayNumber) {
            case 0:
                toolbar.setTitle(groupTitle);
                break;
            case 1:
                toolbar.setTitle(context.getString(R.string.Monday));
                break;
            case 2:
                toolbar.setTitle(context.getString(R.string.Tuesday));
                break;
            case 3:
                toolbar.setTitle(context.getString(R.string.Wednesday));
                break;
            case 4:
                toolbar.setTitle(context.getString(R.string.Thursday));
                break;
            case 5:
                toolbar.setTitle(context.getString(R.string.Friday));
                break;
        }
    }
}
