package geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo
import geek.owl.com.ua.KNUSchedule.Repository.WeekPojo
import geek.owl.com.ua.KNUSchedule.ViewModel.ScopedViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleViewModel : ScopedViewModel() {

  val action = MutableLiveData<String>()
  val scheduleRepo: ScheduleRepo = ScheduleRepo(action)


}
