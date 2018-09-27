package geek.owl.com.ua.KNUSchedule.Util

import androidx.annotation.StringRes
import geek.owl.com.ua.KNUSchedule.R

class StaticVariables{
     companion object {
         const val ERROR :String = "Error"
         const val TIMEOUT : String = "TimeOut"
         const val UNKNOWN_HOST : String = "UNKNOWN_HOST"
         const val LOADING : String = "Loading"
         const val LOADED : String = "Loaded"
         const val UNKNOWN : String = "Unknown"
         const val WEEK_NUMBER: String = "Week_number"
         const val CURRENT_FACULTY : String = "CurrentFaculty"
         const val CURRENT_GROUP : String = "CurrentGroup"

     }

}
 enum class WeekDays(@StringRes int: Int) {
     MONDAY(R.string.Day1),
     TUESDAY(R.string.Day2),
     WEDNESDAY(R.string.Day3),
     THURSDAY(R.string.Day4),
     FRIDAY(R.string.Day5),
     SATURDAY(R.string.Day6);

    @StringRes
    private var text : Int = int

     @StringRes
     fun getText()= text
 }