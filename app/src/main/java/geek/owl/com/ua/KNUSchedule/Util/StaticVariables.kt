package geek.owl.com.ua.KNUSchedule.Util

import androidx.annotation.StringRes
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import geek.owl.com.ua.KNUSchedule.AppClass
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.ClassTime
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class StaticVariables {
  companion object {
    const val ERROR: String = "Error"
    const val TIMEOUT: String = "TimeOut"
    const val UNKNOWN_HOST: String = "UNKNOWN_HOST"
    const val LOADING: String = "Loading"
    const val LOADED: String = "Loaded"
    const val UNKNOWN: String = "Unknown"
    const val CURRENT_WEEK: String = "Week_number"
    const val CURRENT_FACULTY: String = "CurrentFaculty"
    const val CURRENT_GROUP: String = "CurrentGroup"
    const val CURRENT_DAY: String = "CurrentDay"
    const val NOTIFICATION:String = "NotificationSetting"
    const val WEEK_COUNT:String = "WeekCount"
  }

}

enum class WeekDays(@StringRes int: Int) {
  MONDAY(R.string.Monday),
  TUESDAY(R.string.Tuesday),
  WEDNESDAY(R.string.Wednesday),
  THURSDAY(R.string.Thursday),
  FRIDAY(R.string.Friday),
  SATURDAY(R.string.Saturday);

  @StringRes
  private var text: Int = int

  @StringRes
  fun getText() = text

  }


fun getDayTitle(number: Int): String {

  val resources = AppClass.INSTANCE.resources
  return when (number) {
    1 -> {
      resources.getString(R.string.Monday)
    }
    2 -> {
      resources.getString(R.string.Tuesday)
    }
    3 -> {
      resources.getString(R.string.Wednesday)
    }
    4 -> {
      resources.getString(R.string.Thursday)
    }
    5 -> {
      resources.getString(R.string.Friday)
    }
    6 -> {
      resources.getString(R.string.Saturday)
    }
    else -> {
      ""
    }
  }
}

class TimeTypeConverter{

  @TypeConverter
  fun toLocalTime(time:String):LocalTime = LocalTime.parse(time)

  @TypeConverter
  fun fromLocalTime(localTime:LocalTime):String = localTime.toString()
}