package com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.ApplicationClass;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties.Faculty;
import com.knu.krasn.knuscheduler.Presenter.Events.ConnectionEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.ErrorEvent;
import com.knu.krasn.knuscheduler.Presenter.Events.GettingGroupsEvent;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.mindorks.nybus.NYBus;

import java.util.ArrayList;
import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.knu.krasn.knuscheduler.ApplicationClass.getRealm;
import static com.knu.krasn.knuscheduler.Presenter.Utils.ServiceUtils.NetworkConnectionChecker.isOnline;

/**
 * Created by krasn on 9/3/2017.
 */

public class FacultyRecyclerAdapter extends RecyclerView.Adapter<FacultyRecyclerAdapter.ItemHolder> {
    private List<Faculty> faculty = new ArrayList<>();
    private LayoutInflater inflater = null;
    private Context context;
    private NetworkService networkService;

    public FacultyRecyclerAdapter(Context context, List<Faculty> items, NetworkService networkService) {

        faculty = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.networkService = networkService;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new ItemHolder(view, networkService, faculty);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {

        String fac = faculty.get(position).getName();
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

    public void updateData(RealmResults<Faculty> newData) {
        faculty = newData;
        notifyDataSetChanged();
    }

    public void clearData() {
        faculty.clear();
        notifyDataSetChanged();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;

        ItemHolder(View itemView, final NetworkService networkService, List<Faculty> faculties) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.group_card);
            cardView.setOnClickListener(view -> {
                Realm realm = getRealm();
                if (realm.where(Faculty.class).equalTo("name", title.getText().toString()).findFirst().getGroups().isEmpty()) {
//                    NetworkConnectionChecker nc = new NetworkConnectionChecker(ApplicationClass.getContext());
                    if (isOnline(ApplicationClass.getContext())) {
                        NYBus.get().post(new ErrorEvent("Кликнулось"));
                        networkService.getGroups(faculties.get(getAdapterPosition()).getId());
                        cardView.setEnabled(false);
                    } else {
                        NYBus.get().post(new ConnectionEvent());
                        cardView.setEnabled(true);
                    }
                } else {
                    NYBus.get().post(new GettingGroupsEvent(faculties.get(getAdapterPosition()).getGroups()));
                    cardView.setEnabled(false);
                }
            });
        }
    }

}
