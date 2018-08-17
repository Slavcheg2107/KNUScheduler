package com.knu.krasn.knuscheduler.ViewModel.ScheduleViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.knu.krasn.knuscheduler.Repository.SchedulePojo
import com.knu.krasn.knuscheduler.Repository.ScheduleRepo.ScheduleRepo
import com.knu.krasn.knuscheduler.Util.Action

class ScheduleViewModel : ViewModel() {

    private val actionLiveData = MutableLiveData<Action>()
    private val scheduleRepo: ScheduleRepo = ScheduleRepo(actionLiveData)

    fun getSchedule(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>> = scheduleRepo.getScheduleLivedata(group, day, week)

}