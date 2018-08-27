package geek.owl.com.ua.KNUSchedule.ViewModel.GroupViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.owl.krasn.KNUSchedule.Repository.GroupPojo
import com.owl.krasn.KNUSchedule.Repository.GroupRepo.GroupRepo

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