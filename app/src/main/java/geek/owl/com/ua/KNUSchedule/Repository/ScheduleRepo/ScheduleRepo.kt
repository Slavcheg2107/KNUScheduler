package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.WeekPojo
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import geek.owl.com.ua.KNUSchedule.Util.Network.ErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScheduleRepo(val action: MutableLiveData<String>) {

  var database = geek.owl.com.ua.KNUSchedule.AppClass.database.getScheduleDao()

  val group:MutableLiveData<String> = MutableLiveData()

  private val daoData:LiveData<List<SchedulePojo>> = Transformations.switchMap(group){ it->
    GlobalScope.launch(Dispatchers.IO){updateSchedule(it)}
    group.value?.let{database.getSchedule(group = it)}
  }

  var dayListLiveData: LiveData<List<WeekPojo>> =  Transformations.switchMap(daoData) { it ->
    val dayList = ArrayList<DayPojo>()
    it.filter { it.week == 1 }.groupBy { it.day }.forEach {entry->
      dayList.add(DayPojo(entry.key).also{it.scheduleList = entry.value})
    }
    val week1 = WeekPojo(dayList, 1)
    dayList.clear()
    it.filter { it.week == 2 }.groupBy { it.day }.forEach{entry->
      dayList.add(DayPojo(entry.key).also { it.scheduleList = entry.value })
    }
    val week2 = WeekPojo(dayList, 2)
    val weekLiveData = MutableLiveData<List<WeekPojo>>()
    weekLiveData.value = (listOf(week1, week2))
    return@switchMap weekLiveData
  }

  private val apiService by lazy {
    ApiService.createApi()
  }

  private suspend fun updateSchedule(group: String) {
    val job = apiService.getSchedule(group)
      try {
        val response = job.await()
        if (response.isSuccessful) {
          response.body()?.schedules?.let { list ->
            list.forEach { if (it.subgroup == null) it.subgroup = "" }
            database.insertSchedules(list)

          }
        }
      } catch (e: Exception) {
        action.postValue(ErrorHandler.getMessage(e))
      }
  }

  fun searchSchedule(query: String, take: Int, offset: Int) {
//    val job = apiService.getAdvanceSchedule(query, take, offset)
//      val response = job.await()
//
//      try {
//        response.body().let {
//          searchableData.postValue(it?.schedules)
//        }
//      } catch (e: Exception) {
//        action.postValue(ErrorHandler.getMessage(e))
//      }
  }
}