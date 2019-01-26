package geek.owl.com.ua.KNUSchedule.ViewModel.FacultyViewModel

import androidx.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo.FacultyRepo
import geek.owl.com.ua.KNUSchedule.Repository.Result
import geek.owl.com.ua.KNUSchedule.ViewModel.ScopedViewModel
import kotlinx.coroutines.launch

class FacultyViewModel : ScopedViewModel() {
  var actionLiveData: MutableLiveData<String> = MutableLiveData()
  private val facultyRepo = FacultyRepo(action = actionLiveData)
  var faculties = facultyRepo.getFaculties()

  fun updateFaculties() {
    launch {
      facultyRepo.updateFaculties()
    }
  }
}