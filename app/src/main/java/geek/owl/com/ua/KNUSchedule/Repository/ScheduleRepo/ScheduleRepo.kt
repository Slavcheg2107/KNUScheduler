package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import geek.owl.com.ua.KNUSchedule.Util.Network.ErrorHandler
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope

import kotlinx.coroutines.experimental.launch

class ScheduleRepo(val action: MutableLiveData<String>) {
    private val dayListLiveData = MutableLiveData<List<DayPojo>>()
    lateinit var data: LiveData<List<SchedulePojo>>
    private val searchableData: MutableLiveData<List<SchedulePojo>> = MutableLiveData()
    private val apiService by lazy {
        ApiService.createApi()
    }
    var database = geek.owl.com.ua.KNUSchedule.AppClass.database.getScheduleDao()

    fun getScheduleLiveData(group: String, day: Int, week: Int): MutableLiveData<List<SchedulePojo>> {
        return database.getSchedule(group, day, week) as MutableLiveData<List<SchedulePojo>>
    }

    fun getScheduleLiveData(group: String, week: Int): LiveData<List<SchedulePojo>> {
        updateSchedule(group)
        return database.getSchedule(group, week)
    }

    fun updateSchedule(group: String) {
        val job = apiService.getSchedule(group)
        GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
            val response = job.await()
            try {
                if (response.isSuccessful) {
                    response.body()?.schedules?.let {
                        it.forEach {
                            if (it.subgroup == null) it.subgroup = ""
                        }
                        database.insertSchedules(it)
                    }
                }
            } catch (e: Exception) {
                action.postValue(ErrorHandler.getMessage(e))
            }
        })

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