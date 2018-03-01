package com.knu.krasn.knuscheduler.Presenter.Adapters.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedule;
import com.knu.krasn.knuscheduler.Presenter.Events.MoveToNextEvent;
import com.mindorks.nybus.NYBus;

import java.util.List;
import java.util.Locale;

import geek.owl.com.ua.KNUSchedule.R;

import static com.knu.krasn.knuscheduler.ApplicationClass.getDay;


/**
 * Created by krasn on 9/3/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ItemHolder> implements BaseRecyclerAdapter {
    private final int showType;
    private List<Schedule> schedules;
    private LayoutInflater inflater = null;
    private Context context;


    public ScheduleRecyclerAdapter(Context context, List<Schedule> schedules, int showType) {
        this.showType = showType;
        this.schedules = schedules;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.schedule_card_item, parent, false);
        return new ItemHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        Log.e("teach", schedules.get(position).getTeachers());
        itemHolder.lessonType.setText(schedules.get(position).getLessontype());
        itemHolder.lessonName.setText(schedules.get(position).getDiscipline());
        itemHolder.room.setText(String.format(context.getString(R.string.room_corps_number), schedules.get(position).getRoom(), schedules.get(position).getCorps()));
        String teachers = schedules.get(position).getTeachers().replace(",", "\n");
        itemHolder.teacher.setText(teachers);

        itemHolder.lessonNumber.setText(String.format(context.getResources().getString(R.string.lesson_number_time)
                , schedules.get(position).getLesson()
                , schedules.get(position).getBeginTime(), schedules.get(position).getEndTime()));
        String subgroup = schedules.get(position).getSubgroup();
        if (subgroup != null) {
            itemHolder.subgroup.setText(schedules.get(position).getSubgroup());
        } else {
            itemHolder.subgroup.setVisibility(View.GONE);
        }

        if (showType == 1) {
            itemHolder.searchData.setText(String.format(Locale.getDefault(),
                    "%s\n%s\n%s",
                    getDay(schedules.get(position).getDay()), String.format("%s %d", context.getString(R.string.week), schedules.get(position).getWeek()), schedules.get(position).getGroup()));
        } else {
            itemHolder.searchData.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void addData(List newData) {
        this.schedules.addAll(newData);
        notifyDataSetChanged();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void updateData(List newData) {
        this.schedules = newData;
        notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        this.schedules.clear();
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
        TextView searchData;

        ItemHolder(View itemView) {
            super(itemView);
            searchData = itemView.findViewById(R.id.searchData);
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
