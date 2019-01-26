package geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel

import androidx.lifecycle.MutableLiveData
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleRepo
import geek.owl.com.ua.KNUSchedule.ViewModel.ScopedViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchViewModel : ScopedViewModel(){
    val action = MutableLiveData<String>()
    val repo = ScheduleRepo(action)
    fun getSchedule(query:String, page:Int) = launch(coroutineContext) { repo.searchSchedule(query, page) }
}