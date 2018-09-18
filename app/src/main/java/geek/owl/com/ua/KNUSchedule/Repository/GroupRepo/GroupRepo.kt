package geek.owl.com.ua.KNUSchedule.Repository.GroupRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import geek.owl.com.ua.KNUSchedule.Util.Network.ErrorHandler
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

import java.lang.Exception

class GroupRepo(val action: MutableLiveData<String>) {
    var currentFacultyId: Long = 0
    private val apiService by lazy {
        ApiService.createApi()
    }
    private val database = geek.owl.com.ua.KNUSchedule.AppClass.database.getGroupDao()


    fun getGroupLiveData(facultyId: Long): LiveData<List<GroupPojo>> {
        currentFacultyId = facultyId
        updateGroups()
        return database.getGroups(currentFacultyId.toString())
    }

    private fun updateGroups() {
        GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
            val request = apiService.getGroups(currentFacultyId.toString())
            val response = request.await()

            try {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        it.groups.forEach { group ->
                            group.facultyId = currentFacultyId.toString()
                        }
                        database.insertGroups(it.groups)
                    }
                }
            } catch (e: Exception) {
                action.postValue(ErrorHandler.getMessage(e))

            }
        })

    }

    fun refresh() {
        updateGroups()
    }

//    fun search(p0: String?) {
//        launch {
//            val job = apiService.findGroup(p0)
//            val response = job.await()
//
//        }
//    }
}