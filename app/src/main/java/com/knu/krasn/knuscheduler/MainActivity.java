package com.knu.krasn.knuscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.knu.krasn.knuscheduler.Adapters.FacultyRecyclerAdapter;
import com.knu.krasn.knuscheduler.Adapters.GroupRecyclerAdapter;
import com.knu.krasn.knuscheduler.Decor.GridSpacingItemDecoration;
import com.knu.krasn.knuscheduler.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Events.MoveToNextEvent;
import com.knu.krasn.knuscheduler.Models.Facultet;
import com.knu.krasn.knuscheduler.Models.GroupModel.Group;
import com.knu.krasn.knuscheduler.Network.NetworkService;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.thread.NYThread;

import java.util.ArrayList;
import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    FacultyRecyclerAdapter facultyRecyclerAdapter;
    GroupRecyclerAdapter groupRecyclerAdapter;
    RecyclerView recyclerView;
    NetworkService networkService;
    List<Facultet> facultets;
    List<Group> groups;
    TextView mainHeader;
    RelativeLayout loadingWheel;
    ProgressBar pb;
    Realm realm;
    String groupTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NYBus.get().register(this);
        pb = findViewById(R.id.progressBar);
        pb.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        realm = ApplicationClass.getRealm();
        SharedPreferences prefs = ApplicationClass.getPreferences();
        if(getIntent().hasExtra(getString(R.string.Reload))){
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            prefs.edit().putString(getString(R.string.GroupLoaded), "").apply();
        }
        groupTitle = prefs.getString(getString(R.string.GroupLoaded), "");
        if(!groupTitle.equals("")){
            NYBus.get().post(new MoveToNextEvent(groupTitle));
        }
        recyclerView = findViewById(R.id.recycler_view);
        mainHeader = findViewById(R.id.header);
        facultets = new ArrayList<>();
        if(realm.where(Facultet.class).findAll().isEmpty()) {
                facultets.add(new Facultet("ФІТ"));
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(facultets);
                realm.commitTransaction();
        }else{
            facultets = realm.where(Facultet.class).findAll();
        }
        networkService = new NetworkService();
        setupRecyclerView();
        if(!getIntent().hasExtra("getGroup")) {
            facultyRecyclerAdapter = new FacultyRecyclerAdapter(this, facultets, networkService);
            recyclerView.setAdapter(facultyRecyclerAdapter);
        }else{
            NYBus.get().post(new GettingGroupsEvent());
        }
        loadingWheel = findViewById(R.id.wheel);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(2,10,false,getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
    }

    @Override()
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        NYBus.get().unregister(this);
        networkService.mCompositeDisposable.clear();
    }

    @Subscribe(threadType = NYThread.MAIN)
    public void onGettingGroupsEvent(GettingGroupsEvent event){
        mainHeader.setVisibility(View.GONE);
        groups = realm.where(Group.class).findAll();
        groupRecyclerAdapter = new GroupRecyclerAdapter(this, groups, networkService );
        recyclerView.setAdapter(groupRecyclerAdapter);
        if(loadingWheel.isShown()) {
            loadingWheel.setVisibility(View.GONE);
        }
        groupRecyclerAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event){
        if(event.getError().equals("Кликнулось")){
            loadingWheel.setVisibility(View.VISIBLE);
        }
        Log.e("ErrorEvent", event.getError());
    }
    @Subscribe
    public void onConnectionEvent(ConnectionEvent event){
        Toast.makeText(getApplicationContext(),"Немає з'єднання", Toast.LENGTH_LONG).show();
    }
    @Subscribe
    public void onMessageEvent(MoveToNextEvent event){
        Intent i = new Intent(this, ScheduleActivity.class);
        i.putExtra("group", event.getMessage());
        startActivity(i);
        finish();
    }

}



