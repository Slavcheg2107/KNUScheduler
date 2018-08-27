package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import android.arch.lifecycle.MutableLiveData
import com.owl.krasn.KNUSchedule.Repository.DayPojo
import com.owl.krasn.KNUSchedule.Util.ErrorHandler
import com.owl.krasn.KNUSchedule.Repository.SchedulePojo
import com.owl.krasn.KNUSchedule.Util.ApiService
import kotlinx.coroutines.experimental.launch

class ScheduleRepo(val action: MutableLiveData<String>) {

    private val searchableData: MutableLiveData<List<SchedulePojo>> = MutableLiveData()
    private val apiService by lazy {
        ApiService.createApi()
    }
    var database = geek.owl.com.ua.KNUSchedule.AppClass.database.getScheduleDao()

    fun getScheduleLiveData(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>> {
        return database.getSchedule(group, day, week) as MutableLiveData<List<SchedulePojo>>
    }

    fun getScheduleLiveData(group: String, week: Int): MutableLiveData<List<DayPojo>> {
        val data =  database.getSchedule(group, week) as MutableLiveData<List<SchedulePojo>>
        val data1 = MutableLiveData<List<DayPojo>>()
        data.value?.let { it ->
            var items = ArrayList<DayPojo>()
            var days = HashSet<Int>()

            it.forEach {
                if(it.week == week) {
                    days.add(it.day)
                }
            }
             days.forEach { day ->
                 data.value?.let { schedule ->
                     it.forEach {
                         if(it.day == day){

                         }
                     }
                 }
                 items.add(DayPojo(it).also {
                     it.scheduleList
                 })
             }
        }
        return
    }

    fun searchSchedule(query: String, take: Int, offset: Int) {
        val job = apiService.getAdvanceSchedule(query, take, offset)
        launch {
            val response = job.await()

            try {
                response.body()?.let {
                    searchableData.postValue(it.schedules)
                }
            } catch (e: Exception) {
                action.postValue(ErrorHandler.getMessage(e))
            }
        }
    }
}