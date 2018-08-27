package geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo

class ScheduleViewModel : ViewModel() {

    val action = MutableLiveData<String>()
    private val scheduleRepo: ScheduleRepo = ScheduleRepo(action)

    fun getSchedule(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>>
            = scheduleRepo.getScheduleLiveData(group, day, week)
    fun getSchedule(group: String, week: Int): MutableLiveData<List<DayPojo>>
            = scheduleRepo.getScheduleLiveData(group, week)

}