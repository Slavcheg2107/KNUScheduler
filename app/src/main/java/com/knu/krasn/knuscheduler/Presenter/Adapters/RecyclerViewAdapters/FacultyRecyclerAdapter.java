package com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Facultet;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Group;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.knu.krasn.knuscheduler.Utils.NetworkConnectionChecker;
import com.mindorks.nybus.NYBus;

import java.util.ArrayList;
import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;
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
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.group_card);
            cardView.setOnClickListener(view -> {
                Realm realm = ApplicationClass.getRealm();
                if (realm.where(Group.class).findAll().isEmpty()) {
                    NetworkConnectionChecker nc = new NetworkConnectionChecker(ApplicationClass.getContext());
                    if (nc.isOnline()) {
                        NYBus.get().post(new ErrorEvent("Кликнулось"));
                        networkService.getGroups();
                        cardView.setEnabled(false);
                    } else {
                        NYBus.get().post(new ConnectionEvent());
                    }
                } else {
                    NYBus.get().post(new GettingGroupsEvent());

                }
            });
        }
    }
}
