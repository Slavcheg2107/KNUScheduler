package com.knu.krasn.knuscheduler.Util

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.FacultyRepo.FacultyDao
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.GroupRepo.GroupDao
import com.knu.krasn.knuscheduler.Repository.SchedulePojo
import com.knu.krasn.knuscheduler.Repository.ScheduleRepo.ScheduleDao

@Database(entities = [(FacultyPojo::class), (GroupPojo::class), (SchedulePojo::class)], version = 2, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun getFacultyDao(): FacultyDao
    abstract fun getScheduleDao(): ScheduleDao
    abstract fun getGroupDao(): GroupDao
}