package geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.SparseArray
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo

class ScheduleViewModel : ViewModel() {

    val action = MutableLiveData<String>()
    private val scheduleRepo: ScheduleRepo = ScheduleRepo(action)
    private val daysLiveData : MutableLiveData<List<DayPojo>> = MutableLiveData()

    fun getSchedule(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>>
            = scheduleRepo.getScheduleLiveData(group, day, week)

    fun getSchedule(group: String, week: Int): LiveData<List<DayPojo>>
            = Transformations.switchMap(scheduleRepo.getScheduleLiveData(group, week)) { it ->
        val dayList = ArrayList<DayPojo>()
        it.let { list ->
            val dayMap = SparseArray<ArrayList<SchedulePojo>>()
            list.forEach { item ->
                if (dayMap.get(item.day) != null) {
                    dayMap.get(item.day).add(item)
                } else dayMap.put(item.day, ArrayList())
                        .also { dayMap.get(item.day).add(item) }
            }
            for (i in 0 until dayMap.size()-1) {
                dayList.add(DayPojo(dayMap.keyAt(i)).also {
                    it.scheduleList = dayMap.valueAt(i)
                    it.weekNumber = dayMap.valueAt(i)[0].week
                })
            }
            daysLiveData.postValue( dayList)
        }
        return@switchMap daysLiveData
    }


}