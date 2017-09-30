package com.example.krasn.knuscheduler.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krasn.knuscheduler.ApplicationClass;
import com.example.krasn.knuscheduler.Events.ConnectionEvent;
import com.example.krasn.knuscheduler.Events.ErrorEvent;
import com.example.krasn.knuscheduler.Events.GettingGroupsEvent;
import com.example.krasn.knuscheduler.Models.Facultet;
import com.example.krasn.knuscheduler.Models.GroupModel.Group;
import com.example.krasn.knuscheduler.Network.NetworkConnection;
import com.example.krasn.knuscheduler.Network.NetworkService;
import com.example.krasn.knuscheduler.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by krasn on 9/3/2017.
 */

public class FacultyRecyclerAdapter extends RecyclerView.Adapter<FacultyRecyclerAdapter.ItemHolder> {
    private List<Facultet> faculty = new ArrayList<>();
    private LayoutInflater inflater = null;
    private List<Group> groups = new ArrayList<>();
    private Context context;
    private NetworkService networkService;

    public FacultyRecyclerAdapter(Context context, List<Facultet> items, NetworkService networkService) {

        this.faculty = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.networkService = networkService;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new ItemHolder(view, networkService);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {

        String fac = faculty.get(position).getTitle();
        itemHolder.title.setText(fac);

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (faculty == null) ? 0 : faculty.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;
        ItemHolder(View itemView, final NetworkService networkService) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            cardView = (CardView) itemView.findViewById(R.id.group_card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Realm realm = ApplicationClass.getRealm();
                    if(realm.where(Group.class).findAll().isEmpty()) {
                        NetworkConnection nc = new NetworkConnection(ApplicationClass.getContext());
                        if(nc.isOnline()) {
                            EventBus.getDefault().post(new ErrorEvent("Кликнулось"));
                            networkService.getGroups();
                        }else{
                            EventBus.getDefault().post(new ConnectionEvent());
                        }
                    }
                    else{
                        EventBus.getDefault().post(new GettingGroupsEvent());
                    }

                }
            });
        }
    }
}
