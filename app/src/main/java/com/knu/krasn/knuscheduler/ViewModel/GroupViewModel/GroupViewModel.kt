package com.knu.krasn.knuscheduler.ViewModel.GroupViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Repository.GroupRepo.GroupRepo
import com.knu.krasn.knuscheduler.Util.Action

class GroupViewModel : ViewModel() {
    val actionLiveData = MutableLiveData<Action>()
    private val groupRepo: GroupRepo = GroupRepo(actionLiveData)

    fun getGroupLiveData(id: Long): LiveData<List<GroupPojo>> = groupRepo.getGroupLiveData(id)
    fun search(p0: String?) {
//        groupRepo.search(p0)
    }

    fun refresh() {
        groupRepo.refresh()
    }

}