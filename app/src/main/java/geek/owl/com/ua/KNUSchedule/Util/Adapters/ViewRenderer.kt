package geek.owl.com.ua.KNUSchedule.Util.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.*

class ViewRenderer {
    fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.faculty_item, parent, false)
        return when (viewType) {
            ItemType.FACULTY.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.faculty_item, parent, false)
                FacultyViewHolder(view)
            }
            ItemType.GROUP.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
                GroupViewHolder(view)
            }
            ItemType.WEEK.ordinal->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.week_item, parent, false)
                WeekViewHolder(view)
            }
            ItemType.DAY.ordinal ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
                DayViewHolder(view)
            }
            else -> {

                FacultyViewHolder(view)
            }
        }

    }

    inner class FacultyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView? = view.findViewById(R.id.title)

        fun bind(item: FacultyPojo, itemClickListener: OnItemClick) {
            title?.text = item.name
            itemView.setOnClickListener { itemClickListener.onClick(item) }
        }
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(R.id.title)
        fun bind(item: GroupPojo, itemClickListener: OnItemClick) {
            title?.text = item.name
            itemView.setOnClickListener { itemClickListener.onClick(item) }
        }
    }

    inner class WeekViewHolder(view:View):RecyclerView.ViewHolder(view){


        val title: TextView? = view.findViewById(R.id.title)

        fun bind(title: String, itemClickListener: OnItemClick) {
            this.title?.text = title
        }
    }

    inner class DayViewHolder(view:View):RecyclerView.ViewHolder(view){

        fun bind(item:DayPojo, itemClickListener: OnItemClick){

        }
    }
    inner class ScheduleViewHolder(view:View) : RecyclerView.ViewHolder(view){

        fun bind(item : SchedulePojo, itemClickListener: OnItemClick){
        }
    }

}


