package com.knu.krasn.knuscheduler.View.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import geek.owl.com.ua.KNUSchedule.R

class ScheduleSearchFragemtn : Fragment() {

    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        initRecyclerView()

        return view
    }

    private fun initRecyclerView() {
        recyclerView = view!!.findViewById(R.id.recycler_view)
        val lm = GridLayoutManager(context, 2)
        lm.isItemPrefetchEnabled
    }
}