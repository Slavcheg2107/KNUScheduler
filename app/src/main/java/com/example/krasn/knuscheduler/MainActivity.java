package com.example.krasn.knuscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krasn.knuscheduler.Adapters.FacultyRecyclerAdapter;
import com.example.krasn.knuscheduler.Adapters.GroupRecyclerAdapter;
import com.example.krasn.knuscheduler.Decor.GridSpacingItemDecoration;
import com.example.krasn.knuscheduler.Events.ConnectionEvent;
import com.example.krasn.knuscheduler.Events.ErrorEvent;
import com.example.krasn.knuscheduler.Events.GettingGroupsEvent;
import com.example.krasn.knuscheduler.Events.MessageEvent;
import com.example.krasn.knuscheduler.Models.Facultet;
import com.example.krasn.knuscheduler.Models.GroupModel.Group;
import com.example.krasn.knuscheduler.Network.NetworkService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
    Realm realm;
    String groupTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ApplicationClass.getRealm();
        EventBus.getDefault().register(this);
        realm = ApplicationClass.getRealm();
        SharedPreferences prefs = ApplicationClass.getPreferences();
        groupTitle = prefs.getString("GroupLoaded", "");
        if(!groupTitle.equals("")){
            EventBus.getDefault().post(new MessageEvent(groupTitle));
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainHeader = (TextView) findViewById(R.id.header);
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration().getItemDecor(2,10,false,getResources()));
        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        Bundle b = getIntent().getExtras();
        if(!getIntent().hasExtra("getGroup")) {
            facultyRecyclerAdapter = new FacultyRecyclerAdapter(this, facultets, networkService);
            recyclerView.setAdapter(facultyRecyclerAdapter);
        }else{
            EventBus.getDefault().post(new GettingGroupsEvent());
        }
        loadingWheel = (RelativeLayout) findViewById(R.id.wheel);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGettingGroupsEvent(GettingGroupsEvent event){
        mainHeader.setText("Виберіть групу");
        groups = realm.where(Group.class).findAll();
        groupRecyclerAdapter = new GroupRecyclerAdapter(this, groups, networkService );
        recyclerView.setAdapter(groupRecyclerAdapter);
        loadingWheel.setVisibility(View.GONE);
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
    public void onMessageEvent(MessageEvent event){
        Intent i = new Intent(this, ScheduleActivity.class);
        i.putExtra("group", event.getMessage());
        startActivity(i);
        finish();
    }
}



