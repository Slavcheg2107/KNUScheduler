package com.knu.krasn.knuscheduler.Presenter.Adapters.FragmentController;

import android.content.Context;
import android.support.v7.widget.Toolbar;

import com.knu.krasn.knuscheduler.ApplicationClass;

import geek.owl.com.ua.KNUSchedule.R;

/**
 * Created by krasn on 11/20/2017.
 */

public class ToolbarUpdater implements ToolbarUpdaterInterface {
    private String groupTitle;
    private Toolbar toolbar;
    private Context context = ApplicationClass.getContext();

    public ToolbarUpdater(String groupTitle, Toolbar toolbar) {
        this.toolbar = toolbar;
        this.groupTitle = groupTitle;
    }


    @Override
    public void updateToolbar(int dayNumber) {
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
            case 6:
                toolbar.setTitle(context.getString(R.string.Saturday));
                break;
        }
    }
}
