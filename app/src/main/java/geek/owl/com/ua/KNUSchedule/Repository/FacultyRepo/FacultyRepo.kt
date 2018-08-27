package geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.owl.krasn.KNUSchedule.Util.ErrorHandler
import com.owl.krasn.KNUSchedule.Repository.FacultyPojo
import com.owl.krasn.KNUSchedule.Util.ApiService
import kotlinx.coroutines.experimental.launch


import java.lang.Exception

class FacultyRepo(val action: MutableLiveData<String>) {
    private val apiService by lazy {
        ApiService.createApi()
    }
    private val database = geek.owl.com.ua.KNUSchedule.AppClass.database.getFacultyDao()

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
              action.postValue(ErrorHandler.getMessage(e))
            }
        }


    }

    fun getFaculties(): LiveData<List<FacultyPojo>> {
        updateFaculties()
        return database.getAllFaculties()
    }

}