package com.knu.krasn.knuscheduler.Adapters;

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

public class TabAdapter {
    private String groupTitle;
    private Toolbar toolbar;
    private Week1Fragment week1Fragment;
    private Week2Fragment week2Fragment;
    private Context context = ApplicationClass.getContext();
    private List<BaseFragment> fragments = new ArrayList<>();


    public TabAdapter(String groupTitle, Toolbar toolbar) {
        this.toolbar = toolbar;
        this.groupTitle = groupTitle;
    }

    public List<BaseFragment> selectTab(int tabId, FragmentManager fm) {

        FragmentTransaction ft = fm.beginTransaction();
        int contentContainer = R.id.contentContainer;
        if (tabId == R.id.tab_week1) {
            week1Fragment = (Week1Fragment) fm.findFragmentByTag(context.getString(R.string.week1_adapter_name));
            if (week1Fragment == null) {
                week1Fragment = Week1Fragment.newInstance();
                ft.add(contentContainer, week1Fragment, context.getString(R.string.week1_adapter_name));
                if (week2Fragment != null) {
                    ft.hide(week2Fragment).commit();
                } else {
                    ft.commit();
                }
            } else {
                ft.show(week1Fragment);
                if (week2Fragment != null) {
                    ft.hide(week2Fragment).commit();
                }
            }
        } else if (tabId == R.id.tab_week2) {
            week2Fragment = (Week2Fragment) fm.findFragmentByTag(context.getString(R.string.week2_adapter_name));
            if (week2Fragment == null) {
                week2Fragment = Week2Fragment.newInstance();
                ft.add(contentContainer, week2Fragment, context.getString(R.string.week2_adapter_name));
                if (week1Fragment != null) {
                    ft.hide(week1Fragment).commit();
                }

            } else {
                ft.show(week2Fragment);
                if (week1Fragment != null) {
                    ft.hide(week1Fragment).commit();
                } else ft.commit();
            }
        }
        fragments.clear();
        fragments.add(week1Fragment);
        fragments.add(week2Fragment);
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
