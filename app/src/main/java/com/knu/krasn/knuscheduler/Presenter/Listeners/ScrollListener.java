package com.knu.krasn.knuscheduler.Presenter.Listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by krasn on 1/21/2018.
 */

public class ScrollListener {

    public ScrollListener(RecyclerView view) {

    }

    public static Observable<Integer> on(RecyclerView view) {
        NetworkService networkService = new NetworkService();
        final PublishSubject<Integer> subject = PublishSubject.create();
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                    subject.onNext(lastCompletelyVisibleItemPosition);
            }
        });
        return subject;
    }
}
