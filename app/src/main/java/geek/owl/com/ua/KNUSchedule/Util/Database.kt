package geek.owl.com.ua.KNUSchedule.Util

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.owl.krasn.KNUSchedule.Repository.FacultyPojo
import com.owl.krasn.KNUSchedule.Repository.FacultyRepo.FacultyDao
import com.owl.krasn.KNUSchedule.Repository.GroupPojo
import com.owl.krasn.KNUSchedule.Repository.GroupRepo.GroupDao
import com.owl.krasn.KNUSchedule.Repository.SchedulePojo
import com.owl.krasn.KNUSchedule.Repository.ScheduleRepo.ScheduleDao

@Database(entities = [(FacultyPojo::class), (GroupPojo::class), (SchedulePojo::class)], version = 4, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun getFacultyDao(): FacultyDao
    abstract fun getScheduleDao(): ScheduleDao
    abstract fun getGroupDao(): GroupDao
}