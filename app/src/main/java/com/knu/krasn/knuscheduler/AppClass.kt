package com.knu.krasn.knuscheduler

import android.app.Application
import android.arch.persistence.room.Room
import com.knu.krasn.knuscheduler.Util.Database

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, Database::class.java, "KNUDb").fallbackToDestructiveMigration().build()

    }

    companion object {
        lateinit var database: Database
    }
}