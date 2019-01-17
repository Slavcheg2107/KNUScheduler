package geek.owl.com.ua.KNUSchedule.ViewModel.DayViewModel

import androidx.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo
import geek.owl.com.ua.KNUSchedule.ViewModel.ScopedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DayViewModel : ScopedViewModel() {

    val action = MutableLiveData<String>()
    val repo = ScheduleRepo(action = action)
    val liveData = repo.dayLiveData


}