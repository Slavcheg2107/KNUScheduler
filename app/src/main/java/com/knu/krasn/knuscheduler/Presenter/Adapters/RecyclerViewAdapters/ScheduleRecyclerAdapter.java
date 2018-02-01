package com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;
import com.knu.krasn.knuscheduler.Presenter.Events.MoveToNextEvent;
import com.knu.krasn.knuscheduler.Presenter.Network.NetworkService;
import com.mindorks.nybus.NYBus;

import java.util.List;

import geek.owl.com.ua.KNUSchedule.R;


/**
 * Created by krasn on 9/3/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ItemHolder> {
    private List<Schedule> schedules;
    private LayoutInflater inflater = null;
    private Context context;
    private NetworkService networkService;



    public ScheduleRecyclerAdapter(Context context, List<Schedule> schedules, NetworkService networkService) {

        this.schedules = schedules;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.networkService = networkService;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.schedule_card_item, parent, false);
        return new ItemHolder(view, networkService);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {

        itemHolder.lessonType.setText(schedules.get(position).getLessontype());
        itemHolder.lessonName.setText(schedules.get(position).getDiscipline());
        itemHolder.room.setText(String.format(context.getString(R.string.room_corps_number), schedules.get(position).getRoom(), schedules.get(position).getCorps()));
        itemHolder.teacher.setText(schedules.get(position).getTeachers());
        itemHolder.lessonNumber.setText(String.format(context.getResources().getString(R.string.lesson_number_time)
                , schedules.get(position).getLesson()
                , schedules.get(position).getBeginTime(), schedules.get(position).getEndTime()));
        String subgroup = schedules.get(position).getSubgroup();
        if (subgroup != null) {
            itemHolder.subgroup.setText(schedules.get(position).getSubgroup());
        } else {
            itemHolder.subgroup.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(List<Schedule> newData) {
        this.schedules = newData;
        notifyDataSetChanged();
    }

    public void addData(List<Schedule> newData) {
        this.schedules.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (schedules == null) ? 0 : schedules.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView lessonType;
        TextView lessonName;
        TextView room;
        TextView teacher;
        CardView cardView;
        TextView lessonNumber;
        TextView subgroup;


        ItemHolder(View itemView, final NetworkService networkService) {
            super(itemView);
            lessonType = itemView.findViewById(R.id.lesson_type);
            lessonName = itemView.findViewById(R.id.lesson);
            lessonNumber = itemView.findViewById(R.id.lesson_number);
            room = itemView.findViewById(R.id.room);
            subgroup = itemView.findViewById(R.id.subgroup);
            teacher = itemView.findViewById(R.id.teacher);
            cardView = itemView.findViewById(R.id.card);
            cardView.setOnClickListener(view -> NYBus.get().post(new MoveToNextEvent("start")));

        }
    }
}
