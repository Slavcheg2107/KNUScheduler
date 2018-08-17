package com.knu.krasn.knuscheduler.Repository.FacultyRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.knu.krasn.knuscheduler.AppClass
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Util.Action
import com.knu.krasn.knuscheduler.Util.ApiService
import kotlinx.coroutines.experimental.launch


import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class FacultyRepo(val action: MutableLiveData<Action>) {
    private val apiService by lazy {
        ApiService.createApi()
    }
    private val database = AppClass.database.getFacultyDao()

    fun updateFaculties() {

        val job = apiService.getFaculties()
        launch {
            val response = job.await()
            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        database.insertFaculties(it.data)
                    }
                }
            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    action.postValue(Action.ERROR)
                }
                if (e is HttpException) {
                    action.postValue(Action.ERROR)
                }
                if (e is SocketTimeoutException) {
                    action.postValue(Action.TIMEOUT)
                }
            }
        }


    }

    fun getFaculties(): LiveData<List<FacultyPojo>> {
        updateFaculties()
        return database.getAllFaculties()
    }

}