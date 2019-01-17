package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import geek.owl.com.ua.KNUSchedule.AppClass.Companion.database
import geek.owl.com.ua.KNUSchedule.Repository.*
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import geek.owl.com.ua.KNUSchedule.Util.Network.getMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response


class ScheduleRepo(val action: MutableLiveData<String>) {
    private val currentList: MutableList<SchedulePojo> = emptyList<SchedulePojo>().toMutableList()

    private var database = geek.owl.com.ua.KNUSchedule.AppClass.database.getScheduleDao()
    private var offset: Int = 0
    private var limit: Int = 100
    val group: MutableLiveData<String> = MutableLiveData()
    val queryData = MutableLiveData<ScheduleQuery>()
    var daoScheduleLiveData: LiveData<List<SchedulePojo>> = Transformations.switchMap(queryData) { query ->
        if (query.page == 1) {
            offset = 0
            limit = 100
        } else {
            offset = +limit
        }
        GlobalScope.launch {
            getSchedule(query.page, query.day, query.week, query.group)
        }
        val list = database.getSchedule( query.day, query.week, query.group)

        list
    }

    var dayLiveData:LiveData<Result<List<SchedulePojo>>> = Transformations.switchMap(daoScheduleLiveData) { data ->

            currentList.clear()
            currentList.addAll(data.orEmpty())
        MutableLiveData<Result<List<SchedulePojo>>>().also {
            it.value = Result.Success(currentList)
        }
    }


    private val daoData: LiveData<List<SchedulePojo>> = Transformations.switchMap(group) { it ->
        GlobalScope.launch(Dispatchers.IO) { updateSchedule(it) }
        group.value?.let { database.getSchedule(group = it) }
    }

    var dayListLiveData: LiveData<List<WeekPojo>> = Transformations.switchMap(daoData) { it ->
        val dayList = ArrayList<DayPojo>()
        it.filter { it.week == 1 }.groupBy { it.day }.forEach { entry ->
            dayList.add(DayPojo(entry.key, 1).also { it.scheduleList = entry.value })
        }
        val week1 = WeekPojo(dayList, 1)
        dayList.clear()
        it.filter { it.week == 2 }.groupBy { it.day }.forEach { entry ->
            dayList.add(DayPojo(entry.key, 2).also { it.scheduleList = entry.value })
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
            action.postValue(e.getMessage())
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


    suspend fun getSchedule(page: Int, dayNumber: Int, weekNumber: Int, group: String) {

        try {
            consumeResponse(apiService.getClassesTime().await()
                    , apiService.getSchedule(DayRequestBody(offset, limit, dayNumber, weekNumber, group)).await())
        } catch (e: Exception) {
            (dayLiveData as MutableLiveData).postValue(Result.Error(e))
        }

    }

    fun consumeResponse(classTime: Response<ClassTimeResponse>, schedule: Response<ScheduleResponse>) {

        if (schedule.isSuccessful) {
            schedule.body()?.schedules?.let { list ->
                list.forEach { lesson ->
                    if (lesson.subgroup == null) lesson.subgroup = ""
                    classTime.body()?.data?.forEachIndexed { index, classTime -> if (classTime.begin == lesson.beginTime) lesson.lessonNumber = index }
                }
                database.insertSchedules(list.filter { it.week == queryData.value?.week && it.group == queryData.value?.group && it.day == queryData.value?.day })
            }
        }
    }
}
