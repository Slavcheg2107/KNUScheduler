package com.knu.krasn.knuscheduler.ViewModel.FacultyViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.knu.krasn.knuscheduler.Repository.FacultyRepo.FacultyRepo
import com.knu.krasn.knuscheduler.Util.Action

class FacultyViewModel : ViewModel() {
    var actionLiveData: MutableLiveData<Action> = MutableLiveData()
    private val facultyRepo = FacultyRepo(action = actionLiveData)
    var faculties = facultyRepo.getFaculties()

    fun updateFaculties() = facultyRepo.updateFaculties()


}