package com.example.krasn.knuscheduler.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krasn.knuscheduler.Models.Schedule.Schedule;
import com.example.krasn.knuscheduler.Network.NetworkService;
import com.example.krasn.knuscheduler.R;

import java.util.List;

/**
 * Created by krasn on 9/3/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ItemHolder> {
    private List<Schedule> schedule;
    private LayoutInflater inflater = null;
    private Context context;
    private NetworkService networkService;


    public ScheduleRecyclerAdapter(Context context, List<Schedule> schedules, NetworkService networkService) {

        this.schedule = schedules;
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
        itemHolder.lessonType.setText(schedule.get(position).getLessontype());
        itemHolder.lessonName.setText(schedule.get(position).getDiscipline());
        itemHolder.room.setText(schedule.get(position).getRoom()+" "+"ауд.");
        itemHolder.teacher.setText(schedule.get(position).getTeachers());
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (schedule == null) ? 0 : schedule.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView lessonType;
        TextView lessonName;
        TextView room;
        TextView teacher;
        CardView cardView;
        ItemHolder(View itemView, final NetworkService networkService) {
            super(itemView);
            lessonType = (TextView) itemView.findViewById(R.id.lesson_type);
            lessonName = (TextView) itemView.findViewById(R.id.lesson);
            room = (TextView) itemView.findViewById(R.id.room);
            teacher = (TextView) itemView.findViewById(R.id.teacher);
            cardView = (CardView) itemView.findViewById(R.id.card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    EventBus.getDefault().post(new MessageEvent("start"));
                }
            });

        }
    }
}
