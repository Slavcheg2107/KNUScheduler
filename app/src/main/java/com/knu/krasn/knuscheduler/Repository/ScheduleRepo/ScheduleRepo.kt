package com.knu.krasn.knuscheduler.Repository.ScheduleRepo

import android.arch.lifecycle.MutableLiveData
import com.knu.krasn.knuscheduler.AppClass
import com.knu.krasn.knuscheduler.Repository.SchedulePojo
import com.knu.krasn.knuscheduler.Util.Action
import com.knu.krasn.knuscheduler.Util.ApiService

class ScheduleRepo(action: MutableLiveData<Action>) {

    private val apiService by lazy {
        ApiService.createApi()
    }
    var database = AppClass.database.getScheduleDao()

    fun getScheduleLivedata(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>> {
        return database.getScheduleForDayAndGroup(group, day, week) as MutableLiveData<List<SchedulePojo>>
    }
}