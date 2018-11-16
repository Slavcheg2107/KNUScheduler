package geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo
import geek.owl.com.ua.KNUSchedule.Repository.WeekPojo
import geek.owl.com.ua.KNUSchedule.ViewModel.ScopedViewModel
import kotlinx.coroutines.launch

class ScheduleViewModel : ScopedViewModel() {

  val action = MutableLiveData<String>()
  private val scheduleRepo: ScheduleRepo = ScheduleRepo(action)
  var weekLiveData: LiveData<List<WeekPojo>> = MutableLiveData()

  fun getSchedule(group: String){
    launch(coroutineContext) {
      weekLiveData = scheduleRepo.getScheduleLiveData(group)
    }
  }

//  fun getSchedule(group: String, week: Int) {
//    launch(coroutineContext) {
//      Transformations.switchMap(scheduleRepo.getScheduleLiveData(group, week)) { it ->
//        val dayList = ArrayList<DayPojo>()
//        it.filter { it.week == 1 }.groupBy { it.day }.forEach {entry->
//          dayList.add(DayPojo(entry.key).also{it.scheduleList = entry.value})
//        }
//        val week1 = WeekPojo(dayList, 1)
//        dayList.clear()
//        it.filter { it.week == 2 }.groupBy { it.day }.forEach{entry->
//          dayList.add(DayPojo(entry.key).also { it.scheduleList = entry.value })
//        }
//        val week2 = WeekPojo(dayList, 2)
//        weekLiveData.postValue(listOf(week1, week2))
//        return@switchMap weekLiveData
//      }
//    }
//  }
}
