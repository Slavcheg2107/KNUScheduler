package geek.owl.com.ua.KNUSchedule

import android.app.Application
import androidx.room.Room
import geek.owl.com.ua.KNUSchedule.Util.Database

class AppClass : Application() {
  override fun onCreate() {
    super.onCreate()
    geek.owl.com.ua.KNUSchedule.AppClass.database = Room.databaseBuilder(this, Database::class.java, "KNUDb").fallbackToDestructiveMigration().build()
    geek.owl.com.ua.KNUSchedule.AppClass.INSTANCE = this
  }

  companion object {
    lateinit var database: Database
    lateinit var INSTANCE: geek.owl.com.ua.KNUSchedule.AppClass
  }
}