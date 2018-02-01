package com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.MoveToNextEvent;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.mindorks.nybus.NYBus;

import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.RealmResults;

import static com.knu.krasn.knuscheduler.ApplicationClass.getContext;
import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NetworkConnectionChecker.isOnline;


/**
 * Created by krasn on 9/3/2017.
 */

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ItemHolder> {
    private List<Group> groups;
    private Context context;
    private LayoutInflater inflater = null;
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


    public void updateData(RealmResults<Group> newData) {
        groups = newData;
        notifyDataSetChanged();
    }

    public void clearData() {
        groups.clear();
        notifyDataSetChanged();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;

        ItemHolder(View itemView, final NetworkService networkService) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.group_card);
            cardView.setOnClickListener(view -> {
                Group group = ApplicationClass.getRealm().where(Group.class).equalTo("title", title.getText().toString()).findFirst();
                if (group != null && group.getWeek1() != null) {
                    NYBus.get().post(new MoveToNextEvent(title.getText().toString()));
                } else {

                    if (isOnline(ApplicationClass.getContext())) {
                        settings.edit().putString(getContext().getResources().getString(R.string.current_group), title.getText().toString()).apply();
                        NYBus.get().post(new MoveToNextEvent(title.getText().toString()));

                    } else {
                        NYBus.get().post(new ConnectionEvent());
                    }
                }
            });
        }
    }
}
