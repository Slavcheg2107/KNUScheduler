package com.knu.krasn.knuscheduler.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.Events.ShowScheduleEvent;
import com.knu.krasn.knuscheduler.Models.DayOfWeek.DayOfWeek;
import com.knu.krasn.knuscheduler.Network.NetworkService;
import com.mindorks.nybus.NYBus;

import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;

/**
 * Created by krasn on 9/3/2017.
 */

public class Week1RecyclerAdapter extends RecyclerView.Adapter<Week1RecyclerAdapter.ItemHolder> {

    private LayoutInflater inflater = null;
    private List<DayOfWeek> days;
    private Context context;
    private NetworkService networkService;


    public Week1RecyclerAdapter(Context context, List<DayOfWeek> daysList, NetworkService networkService) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.networkService = networkService;
        this.days = daysList;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.day_item, parent, false);

        return new ItemHolder(view, days);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {

        switch (days.get(position).getDayNumber()) {
            case 1:
                itemHolder.dayTitle.setText(context.getString(R.string.Day1, days.get(position).getScheduleSize()));
                break;
            case 2:
                itemHolder.dayTitle.setText(context.getString(R.string.Day2, days.get(position).getScheduleSize()));
                break;
            case 3:
                itemHolder.dayTitle.setText(context.getString(R.string.Day3, days.get(position).getScheduleSize()));
                break;
            case 4:
                itemHolder.dayTitle.setText(context.getString(R.string.Day4, days.get(position).getScheduleSize()));
                break;
            case 5:
                itemHolder.dayTitle.setText(context.getString(R.string.Day5, days.get(position).getScheduleSize()));
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView dayTitle;
        CardView cardView;
        List<DayOfWeek> days;

        ItemHolder(View itemView, final List<DayOfWeek> days) {
            super(itemView);
            this.days = days;
            dayTitle = itemView.findViewById(R.id.day_of_week);
            cardView = itemView.findViewById(R.id.day_card);
            cardView.setOnClickListener(view -> NYBus.get().post(new ShowScheduleEvent(days.get(getAdapterPosition()).getDayNumber(), "week1")));

        }
    }
}
