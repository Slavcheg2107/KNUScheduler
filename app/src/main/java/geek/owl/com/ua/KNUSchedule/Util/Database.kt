package geek.owl.com.ua.KNUSchedule.Util

import androidx.room.Database
import androidx.room.RoomDatabase

import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo
import geek.owl.com.ua.KNUSchedule.Repository.FacultyRepo.FacultyDao
import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo
import geek.owl.com.ua.KNUSchedule.Repository.GroupRepo.GroupDao
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleRepo.ScheduleDao

@Database(entities = [(FacultyPojo::class), (GroupPojo::class), (SchedulePojo::class)], version = 8, exportSchema = false)
abstract class Database : RoomDatabase() {
  abstract fun getFacultyDao(): FacultyDao
  abstract fun getScheduleDao(): ScheduleDao
  abstract fun getGroupDao(): GroupDao
}