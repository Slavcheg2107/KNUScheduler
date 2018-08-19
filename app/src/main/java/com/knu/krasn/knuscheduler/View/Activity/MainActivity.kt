package com.knu.krasn.knuscheduler.View.Activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import geek.owl.com.ua.KNUSchedule.R

class MainActivity : AppCompatActivity(), NavHost {

    lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        controller = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun getNavController(): NavController {
        return controller
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this, R.id.nav_host_fragment).navigateUp()

}