package com.knu.krasn.knuscheduler.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.knu.krasn.knuscheduler.Events.MoveToNextEvent;
import com.knu.krasn.knuscheduler.Models.Schedule.Schedule;
import com.knu.krasn.knuscheduler.Network.NetworkService;
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
        itemHolder.room.setText(schedules.get(position).getRoom() + " " + "ауд.");
        itemHolder.teacher.setText(schedules.get(position).getTeachers());
        itemHolder.lessonNumber.setText(String.valueOf(schedules.get(position).getLesson()));
        String time = schedules.get(position).getTime().getBegin();
        itemHolder.time.setText(time);
        Log.e("TIME", schedules.get(position).getTime().getBegin() + schedules.get(position).getTime().getEnd());

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
        TextView time;

        ItemHolder(View itemView, final NetworkService networkService) {
            super(itemView);
            lessonType = itemView.findViewById(R.id.lesson_type);
            lessonName = itemView.findViewById(R.id.lesson);
            lessonNumber = itemView.findViewById(R.id.lesson_number);
            room = itemView.findViewById(R.id.room);
            subgroup = itemView.findViewById(R.id.subgroup);
            teacher = itemView.findViewById(R.id.teacher);
            time = itemView.findViewById(R.id.time_of_lesson);
            cardView = itemView.findViewById(R.id.card);
            cardView.setOnClickListener(view -> NYBus.get().post(new MoveToNextEvent("start")));

        }
    }
}
