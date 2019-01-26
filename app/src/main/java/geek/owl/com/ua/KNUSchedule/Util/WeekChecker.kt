package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_WEEK
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.WEEK_COUNT
import java.util.*

class WeekChecker(val context: Context, workerParams:WorkerParameters ) : Worker(context, workerParams){
    override fun doWork(): Result {
        return if(checkWeekChange())
            Result.SUCCESS
        else Result.FAILURE
    }

    private fun checkWeekChange():Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val previousWeek = prefs.getInt(WEEK_COUNT, -1)
        val currentWeekInYear= Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        if(currentWeekInYear>previousWeek){
            val currentWeek = prefs.getInt(CURRENT_WEEK, -1)
            prefs.edit().putInt(CURRENT_WEEK, if(currentWeek == 1) 2 else if(currentWeek==2){ 1}else -1)
                    .putInt(WEEK_COUNT, currentWeekInYear).apply()
            return true
        }
        return false
    }
}
