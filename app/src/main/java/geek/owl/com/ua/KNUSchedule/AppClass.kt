package geek.owl.com.ua.KNUSchedule

import android.app.Application
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import geek.owl.com.ua.KNUSchedule.Util.Database



class AppClass : Application() {

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    database = Room.databaseBuilder(this, Database::class.java, "KNUDb").fallbackToDestructiveMigration().build()
    INSTANCE = this
  }

  companion object {
    lateinit var database: Database
    lateinit var INSTANCE: AppClass
  }
}
val mFirebaseAnalytics = FirebaseAnalytics.getInstance(AppClass.INSTANCE)

