package geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FacultyRepo(val action: MutableLiveData<String>) {
  private val apiService by lazy {
    ApiService.createApi()
  }
  private val database = geek.owl.com.ua.KNUSchedule.AppClass.database.getFacultyDao()

  fun updateFaculties() {

    val job = apiService.getFaculties()
    GlobalScope.launch {
      val response = job.await()

      if (response.isSuccessful) {
        response.body()?.let {
          database.insertFaculties(it.data)
        }
      }
    }

  }

  fun getFaculties(): LiveData<List<FacultyPojo>> {
    updateFaculties()
    return database.getAllFaculties()
  }


}