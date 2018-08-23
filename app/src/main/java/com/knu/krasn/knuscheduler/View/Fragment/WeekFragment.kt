package com.knu.krasn.knuscheduler.View.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import geek.owl.com.ua.KNUSchedule.R

class WeekFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.week_fragment, container, false)


        return view
    }
}