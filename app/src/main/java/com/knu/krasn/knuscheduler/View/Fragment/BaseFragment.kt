package com.knu.krasn.knuscheduler.View.Fragment

import android.support.v4.app.Fragment

class BaseFragment : Fragment(), OnBackPressedListener {
    override fun onBackPressed(): Boolean {
        return true
    }
}