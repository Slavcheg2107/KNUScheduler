package com.knu.krasn.knuscheduler.Repository.GroupRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.knu.krasn.knuscheduler.AppClass
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Util.Action
import com.knu.krasn.knuscheduler.Util.ApiService
import kotlinx.coroutines.experimental.launch

import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException

class GroupRepo(val action: MutableLiveData<Action>) {
    private val apiService by lazy {
        ApiService.createApi()
    }
    private val database = AppClass.database.getGroupDao()


    fun getGroupLiveData(facultyId: Long): LiveData<List<GroupPojo>> {
        updateGroups(facultyId.toString())
        return database.getGroups(facultyId.toString())
    }

    private fun updateGroups(facultyId: String) {
        launch {
            val request = apiService.getGroups(facultyId)
            val response = request.await()

            try {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val groups = ArrayList<GroupPojo>()
                        it.groups.forEach { group ->
                            val group1 = GroupPojo(group)
                            group1.facultyId = facultyId
                            groups.add(group1)
                        }
                        database.insertGroups(groups)
                    }
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    action.postValue(Action.ERROR)
                }
                if (e is SocketTimeoutException) {
                    action.postValue(Action.TIMEOUT)
                }
            }
        }

    }

//    fun search(p0: String?) {
//        launch {
//            val job = apiService.findGroup(p0)
//            val response = job.await()
//
//        }
//    }
}