package geek.owl.com.ua.KNUSchedule.ViewModel.FacultyViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo.FacultyRepo

class FacultyViewModel : ViewModel() {
    var actionLiveData: MutableLiveData<String> = MutableLiveData()
    private val facultyRepo = FacultyRepo(action = actionLiveData)
    var faculties = facultyRepo.getFaculties()

    fun updateFaculties() = facultyRepo.updateFaculties()


}