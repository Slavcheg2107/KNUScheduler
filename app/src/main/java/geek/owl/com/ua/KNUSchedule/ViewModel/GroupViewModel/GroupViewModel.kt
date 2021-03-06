package geek.owl.com.ua.KNUSchedule.ViewModel.GroupViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo
import geek.owl.com.ua.KNUSchedule.Repository.GroupRepo.GroupRepo

class GroupViewModel : ViewModel() {
  val actionLiveData = MutableLiveData<String>()
  private val groupRepo: GroupRepo = GroupRepo(actionLiveData)

  fun getGroupLiveData(id: Long): LiveData<List<GroupPojo>> = groupRepo.getGroupLiveData(id)
  fun search(p0: String?) {
//        groupRepo.search(p0)
  }

  fun refresh() {
    groupRepo.refresh()
  }

}