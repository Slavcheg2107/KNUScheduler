package com.knu.krasn.knuscheduler.Util

import android.support.v7.util.DiffUtil
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.ItemType
import com.knu.krasn.knuscheduler.Util.Adapters.SimpleAdapter

class KNUDiffUtil(val oldList: List<SimpleAdapter.ItemModel>, val newList: List<SimpleAdapter.ItemModel>) : DiffUtil.Callback() {

    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        val oldItem = oldList[p0]
        val newItem = newList[p1]
        return when (oldList[0].getType()) {
            ItemType.FACULTY.ordinal -> {
                oldItem as FacultyPojo
                newItem as FacultyPojo
                oldItem.id == newItem.id
            }
            ItemType.GROUP.ordinal -> {
                oldItem as GroupPojo
                newItem as GroupPojo
                oldItem.id == newItem.id
            }
            else -> true
        }

    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        val oldItem = oldList[p0]
        val newItem = newList[p1]
        return when (oldList[0].getType()) {
            ItemType.FACULTY.ordinal -> {
                oldItem as FacultyPojo
                newItem as FacultyPojo
                oldItem.name == newItem.name
            }
            ItemType.GROUP.ordinal -> {
                oldItem as GroupPojo
                newItem as GroupPojo
                oldItem.name == newItem.name
            }
            else -> true
        }
    }


}