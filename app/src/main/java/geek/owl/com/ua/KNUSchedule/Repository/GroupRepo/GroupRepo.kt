package geek.owl.com.ua.KNUSchedule.Repository.GroupRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.owl.krasn.KNUSchedule.Util.ErrorHandler
import com.owl.krasn.KNUSchedule.Repository.GroupPojo
import com.owl.krasn.KNUSchedule.Util.ApiService
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
        updateGroups();
        return database.getGroups(currentFacultyId.toString())
    }

    private fun updateGroups() {
        launch {
            val request = apiService.getGroups(currentFacultyId.toString())
            val response = request.await()

            try {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val groups = ArrayList<GroupPojo>()
                        it.groups.forEach { group ->
                            val group1 = GroupPojo(group)
                            group1.facultyId = currentFacultyId.toString()
                            groups.add(group1)
                        }
                        database.insertGroups(groups)
                    }
                }
            } catch (e: Exception) {
                action.postValue(ErrorHandler.getMessage(e))

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