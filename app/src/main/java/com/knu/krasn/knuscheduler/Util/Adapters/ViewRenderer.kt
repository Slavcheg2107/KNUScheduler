package com.knu.krasn.knuscheduler.Util.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.ItemType
import geek.owl.com.ua.KNUSchedule.R

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
}


