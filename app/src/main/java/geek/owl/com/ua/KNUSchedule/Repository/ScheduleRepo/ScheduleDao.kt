package geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import geek.owl.com.ua.KNUSchedule.Repository.ClassTime
import geek.owl.com.ua.KNUSchedule.Repository.Result
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import org.threeten.bp.LocalTime

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

  @Query("SELECT * FROM schedulepojo WHERE `group` = :group and week= :week and day=:day and start > :currentTime ")
  fun getSchedule(group: String, week: Int, day: Int, currentTime: LocalTime):List<SchedulePojo>

  @Query("SELECT * FROM schedulepojo WHERE `group` =:group")
  fun getSchedule(group: String): LiveData<List<SchedulePojo>>


  @Query("DELETE FROM schedulepojo WHERE `group`=:group AND week=:week")
  fun deleteSchedules(group: String, week: Int)

  @Query("SELECT * FROM schedulepojo WHERE `group`=:group AND week =:weekNumber AND day=:dayNumber")
  fun getSchedule( dayNumber: Int, weekNumber: Int, group: String): LiveData<List<SchedulePojo>>

}