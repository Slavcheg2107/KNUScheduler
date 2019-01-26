package geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo
import geek.owl.com.ua.KNUSchedule.Repository.Result
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import geek.owl.com.ua.KNUSchedule.Util.Network.getMessage
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
      try{
      val response = job.await()

      if (response.isSuccessful) {
        response.body()?.let {
          database.insertFaculties(it.data)
        }
      }
    }catch (e:Exception){
        action.postValue(e.getMessage())
      }
    }

  }

  fun getFaculties(): LiveData<List<FacultyPojo>> {
    updateFaculties()
    return database.getAllFaculties()
  }

}