package com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters;

import java.util.List;

/**
 * Created by krasn on 2/10/2018.
 */

public interface BaseRecyclerAdapter {


    void addData(List newData);

    void updateData(List newData);

    void clearData();

}
