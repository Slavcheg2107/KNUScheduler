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
import com.mindorks.nybus.NYBus;

import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;

import static com.knu.krasn.knuscheduler.ApplicationClass.getContext;
import static com.knu.krasn.knuscheduler.ApplicationClass.settings;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NetworkConnectionChecker.isOnline;


/**
 * Created by krasn on 9/3/2017.
 */

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ItemHolder> implements BaseRecyclerAdapter {
    private List<Group> groups;

    private LayoutInflater inflater = null;


    public GroupRecyclerAdapter(Context context, List<Group> groups) {
        this.groups = groups;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new ItemHolder(view);
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

    @SuppressWarnings("unchecked")
    @Override
    public void addData(List newData) {
        groups.addAll(newData);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateData(List newData) {
        groups = newData;
        notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        groups.clear();
        notifyDataSetChanged();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;

        ItemHolder(View itemView) {
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
