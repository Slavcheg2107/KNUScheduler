package com.knu.krasn.knuscheduler.Repository.FacultyRepo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.knu.krasn.knuscheduler.Repository.FacultyPojo

@Dao
interface FacultyDao {


    @Query("SELECT * FROM facultypojo")
    fun getAllFaculties(): LiveData<List<FacultyPojo>>

    @Query("DELETE FROM facultypojo")
    fun deleteFaculties()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFaculties(faculties: List<FacultyPojo>)


}