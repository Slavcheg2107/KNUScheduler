package geek.owl.com.ua.KNUSchedule.Repository.GroupRepo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo

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