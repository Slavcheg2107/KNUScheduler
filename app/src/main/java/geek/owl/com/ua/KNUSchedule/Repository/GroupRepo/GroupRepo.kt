package geek.owl.com.ua.KNUSchedule.Repository.GroupRepo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo
import geek.owl.com.ua.KNUSchedule.Util.Network.ApiService
import geek.owl.com.ua.KNUSchedule.Util.Network.getMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    GlobalScope.launch {
      val request = apiService.getGroups(currentFacultyId.toString())

      try {
        val response = request.await()
        if (response.isSuccessful) {
          response.body()?.let { it ->
            it.groups.forEach { group ->
              group.facultyId = currentFacultyId.toString()
            }
            database.insertGroups(it.groups)
          }
        }
      } catch (e: Exception) {
        action.postValue(e.getMessage())

      }
    }
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