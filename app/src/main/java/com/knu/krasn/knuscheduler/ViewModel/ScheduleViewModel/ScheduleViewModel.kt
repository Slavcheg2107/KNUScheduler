package com.knu.krasn.knuscheduler.ViewModel.ScheduleViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.knu.krasn.knuscheduler.Repository.SchedulePojo
import com.knu.krasn.knuscheduler.Repository.ScheduleRepo.ScheduleRepo

class ScheduleViewModel : ViewModel() {

    val action = MutableLiveData<String>()
    private val scheduleRepo: ScheduleRepo = ScheduleRepo(action)

    fun getSchedule(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>>
            = scheduleRepo.getScheduleLiveData(group, day, week)
    fun getSchedule(group: String, week: Int): MutableLiveData<List<SchedulePojo>>
            = scheduleRepo.getScheduleLiveData(group, week)

}