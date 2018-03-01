package com.knu.krasn.knuscheduler.View;

import android.support.v7.widget.SearchView;

/**
 * Created by krasn on 2/10/2018.
 */

public interface SearchViewActivity {
    void showLoader();

    void hideLoader();

    void setupRecyclerView();

    void setListenersOnSearchView(SearchView searchView);

    void setupSearchView(SearchView searchView);

}
