package geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.SparseArray
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo
import geek.owl.com.ua.KNUSchedule.Repository.WeekPojo

class ScheduleViewModel : ViewModel() {

  val action = MutableLiveData<String>()
  private val scheduleRepo: ScheduleRepo = ScheduleRepo(action)
  private val weekLiveData: MutableLiveData<List<WeekPojo>> = MutableLiveData()

  fun getSchedule(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>> = scheduleRepo.getScheduleLiveData(group, day, week)

  fun getSchedule(group: String, week: Int): LiveData<List<WeekPojo>> = Transformations.switchMap(scheduleRepo.getScheduleLiveData(group, week)) { it ->
    val dayList = ArrayList<DayPojo>()
    var week1 = WeekPojo(ArrayList(), 1)
    var week2 = WeekPojo(ArrayList(), 2)

    it.let { list ->
      val dayMap = SparseArray<ArrayList<SchedulePojo>>()
      list.forEach { schedulePojo ->
        if (dayMap.get(schedulePojo.day) != null) {
          dayMap.get(schedulePojo.day).add(schedulePojo)
        } else dayMap.put(schedulePojo.day, ArrayList())
            .also { dayMap.get(schedulePojo.day).add(schedulePojo) }
      }
      for (i in 0 until dayMap.size() - 1) {
        dayList.add(DayPojo(dayMap.keyAt(i)).also {
          it.scheduleList = dayMap.valueAt(i)
          it.weekNumber = dayMap.valueAt(i)[0].week
        })
      }
      dayList.forEach {
        when (it.weekNumber) {
          1 -> week1.list.add(it)
          2 -> week2.list.add(it)
        }
      }

      val weekList = ArrayList<WeekPojo>().also {
        it.add(week1)
        it.add(week2)
      }

      weekLiveData.postValue(weekList)
    }
    return@switchMap weekLiveData
  }


}