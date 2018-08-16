package com.knu.krasn.knuscheduler.Util.Adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.ItemType


class SimpleAdapter(var data: MutableList<ItemModel>, private val itemClickListener: OnItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemModel {
        fun getType(): Int
    }


    private val renderer: ViewRenderer = ViewRenderer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return renderer.createViewHolder(parent, viewType)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        holder.itemView.setOnClickListener { itemClickListener.onClick(item) }
        when (holder.itemViewType) {
            ItemType.FACULTY.ordinal -> {
                item as FacultyPojo
                holder as ViewRenderer.FacultyViewHolder
                holder.bind(item, itemClickListener)
            }
            ItemType.GROUP.ordinal -> {
                item as GroupPojo
                holder as ViewRenderer.GroupViewHolder
                holder.bind(item, itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {

        return data[position].getType()
    }

}