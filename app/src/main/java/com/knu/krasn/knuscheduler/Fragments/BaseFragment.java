package com.knu.krasn.knuscheduler.Fragments;

import android.support.v7.widget.RecyclerView;

/**
 * Created by krasn on 11/20/2017.
 */

public interface BaseFragment {
     void onBackPressed();

    RecyclerView.Adapter getAdapter();
}
