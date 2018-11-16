package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo

@Dao
interface ScheduleDao {

  @Query("SELECT * FROM schedulepojo WHERE `group` = :group AND day =:day")
  fun getSchedule(group: String, day: Int): LiveData<List<SchedulePojo>>

  @Query("DELETE FROM schedulepojo WHERE `group`=:group AND day =:day AND week=:week")
  fun deleteSchedules(group: String, day: Int, week: Int)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertSchedules(schedules: List<SchedulePojo>)

  @Query("SELECT * FROM schedulepojo WHERE `group` = :group AND week = :week")
  fun getScheduleWithWeek(group: String, week: Int): LiveData<List<SchedulePojo>>

  @Query("SELECT * FROM schedulepojo WHERE `group` = :group")
  fun getSchedule(group: String): LiveData<List<SchedulePojo>>


  @Query("DELETE FROM schedulepojo WHERE `group`=:group AND week=:week")
  fun deleteSchedules(group: String, week: Int)
}