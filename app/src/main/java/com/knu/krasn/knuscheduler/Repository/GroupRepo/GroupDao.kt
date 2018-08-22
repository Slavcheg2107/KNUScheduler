package com.knu.krasn.knuscheduler.Repository.GroupRepo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Repository.GroupPojo

@Dao
interface GroupDao {

    @Query("SELECT * FROM grouppojo WHERE facultyId=:facultyId")
    fun getGroups(facultyId: String): LiveData<List<GroupPojo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroups(groupList: List<GroupPojo>)

    @Delete()
    fun deleteGroups(vararg groupPojo: GroupPojo)

    @Query("DELETE FROM grouppojo")
    fun deleteAllGroups()
}