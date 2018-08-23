package com.knu.krasn.knuscheduler.Util.Adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.Item
import com.knu.krasn.knuscheduler.Repository.ItemType
import com.knu.krasn.knuscheduler.Util.KNUDiffUtil


class SimpleAdapter(var data: MutableList<ItemModel>, private val itemClickListener: OnItemClick) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isFiltering : Boolean = false
    private var filteredData : MutableList<ItemModel> = ArrayList()
    interface ItemModel {
        fun getType(): Int
    }

    private val renderer: ViewRenderer = ViewRenderer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return renderer.createViewHolder(parent, viewType)

    }

    fun filterGroups(s: String) {
        data as MutableList<GroupPojo>
        if(s!="") {
            isFiltering =true
            filteredData = data.filter {
                it as GroupPojo
                it.name.toLowerCase().contains(s.toLowerCase()) || it.name.toLowerCase() == s.toLowerCase()
            }.toMutableList()
            val groupDiffUtil = KNUDiffUtil(data, filteredData)
            val result = DiffUtil.calculateDiff(groupDiffUtil, true)
            result.dispatchUpdatesTo(this)
        }
        else{
            isFiltering = false
            this.notifyDataSetChanged()

        }

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item : ItemModel = if(isFiltering) filteredData[position] else data[position]
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
        return if(!isFiltering){
            data.size
        }else filteredData.size
    }

    override fun getItemViewType(position: Int): Int {

        return if(!isFiltering){
            data[position].getType()
        }else filteredData[position].getType()
    }



}