package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import android.arch.lifecycle.MutableLiveData
import android.util.SparseArray
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Util.ApiService
import geek.owl.com.ua.KNUSchedule.Util.ErrorHandler

import kotlinx.coroutines.experimental.launch

class ScheduleRepo(val action: MutableLiveData<String>) {
    val dayListLiveData = MutableLiveData<List<DayPojo>>()

    private val searchableData: MutableLiveData<List<SchedulePojo>> = MutableLiveData()
    private val apiService by lazy {
        ApiService.createApi()
    }
    var database = geek.owl.com.ua.KNUSchedule.AppClass.database.getScheduleDao()

    fun getScheduleLiveData(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>> {
        return database.getSchedule(group, day, week) as MutableLiveData<List<SchedulePojo>>
    }

    fun getScheduleLiveData(group: String, week: Int): MutableLiveData<List<DayPojo>> {
        val job = launch {
            val dayList = ArrayList<DayPojo>()
            val data =  database.getSchedule(group, week) as MutableLiveData<List<SchedulePojo>>
            data.value?.let { list ->
                val dayMap = SparseArray<ArrayList<SchedulePojo>>()
                list.forEach { item->
                    if(dayMap.get(item.day)!=null){
                        dayMap.get(item.day).add(item)
                    }else dayMap.put(item.day, ArrayList())
                }
                for( i in 0 until dayMap.size())
                    dayList.add(DayPojo(dayMap.keyAt(i)).also {
                        it.scheduleList = dayMap.valueAt(i)
                    })
            }
            dayListLiveData.value = dayList
        }
        
        return dayListLiveData
    }

    fun searchSchedule(query: String, take: Int, offset: Int) {
        val job = apiService.getAdvanceSchedule(query, take, offset)
        launch {
            val response = job.await()

            try {
                response.body().let {
                    searchableData.postValue(it?.schedules)
                }
            } catch (e: Exception) {
                action.postValue(ErrorHandler.getMessage(e))
            }
        }
    }
}