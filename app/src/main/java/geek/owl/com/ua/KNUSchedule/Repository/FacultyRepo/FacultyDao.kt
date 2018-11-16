package geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo

@Dao
interface FacultyDao {


  @Query("SELECT * FROM facultypojo")
  fun getAllFaculties(): LiveData<List<FacultyPojo>>

  @Query("DELETE FROM facultypojo")
  fun deleteFaculties()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertFaculties(faculties: List<FacultyPojo>)

}