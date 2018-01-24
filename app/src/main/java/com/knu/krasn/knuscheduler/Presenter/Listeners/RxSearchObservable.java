package com.knu.krasn.knuscheduler.Presenter.Listeners;

import android.support.v7.widget.SearchView;
import android.view.View;

import com.knu.krasn.knuscheduler.View.Activities.SearchActivity;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by krasn on 1/12/2018.
 */
public class RxSearchObservable {

    private RxSearchObservable() {
        // no instance
    }

    public static Observable<String> fromView(SearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                SearchActivity.loadingWheel.setVisibility(View.VISIBLE);
                subject.onNext(text);
                return true;
            }
        });

        return subject;
    }
}
