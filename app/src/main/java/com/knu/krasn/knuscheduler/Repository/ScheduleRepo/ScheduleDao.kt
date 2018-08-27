package com.knu.krasn.knuscheduler.Repository.ScheduleRepo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.knu.krasn.knuscheduler.Repository.SchedulePojo

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedulepojo WHERE `group` = :group AND day =:day AND week=:week")
    fun getSchedule(group: String, day: Int, week: Int): LiveData<List<SchedulePojo>>

    @Query("DELETE FROM schedulepojo WHERE `group`=:group AND day =:day AND week=:week")
    fun deleteSchedules(group: String, day: Int, week: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedules(schedules: List<SchedulePojo>)

    @Query("SELECT * FROM schedulepojo WHERE `group` = :group AND week = :week")
    fun getSchedule(group: String, week: Int): LiveData<List<SchedulePojo>>

    @Query("DELETE FROM schedulepojo WHERE `group`=:group AND week=:week")
    fun deleteSchedules(group: String, week: Int)
}