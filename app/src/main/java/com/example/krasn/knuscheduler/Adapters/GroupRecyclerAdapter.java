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
import com.example.krasn.knuscheduler.Events.MessageEvent;
import com.example.krasn.knuscheduler.Models.GroupModel.Group;
import com.example.krasn.knuscheduler.Network.NetworkConnection;
import com.example.krasn.knuscheduler.Network.NetworkService;
import com.example.krasn.knuscheduler.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by krasn on 9/3/2017.
 */

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ItemHolder> {
    private LayoutInflater inflater = null;
    List<Group> groups;

    Context context;
    private NetworkService networkService;

    public GroupRecyclerAdapter(Context context, List<Group> groups, NetworkService networkService) {

        this.groups = groups;
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

        itemHolder.title.setText(groups.get(position).getTitle());

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (groups == null) ? 0 : groups.size();
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
                    Group group = ApplicationClass.getRealm().where(Group.class).equalTo("title", title.getText().toString()).findFirst();
                    if (group != null && group.getWeek1() != null) {
                        EventBus.getDefault().post(new MessageEvent(title.getText().toString()));
                    }
                    else{
                        NetworkConnection nc = new NetworkConnection(ApplicationClass.getContext());
                        if(nc.isOnline()){
                            EventBus.getDefault().post(new MessageEvent(title.getText().toString()));
                        }
                        else {
                            EventBus.getDefault().post(new ConnectionEvent());
                        }
                    }
                }
            });
        }
    }
}
