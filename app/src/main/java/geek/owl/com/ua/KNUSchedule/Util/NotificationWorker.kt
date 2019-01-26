package geek.owl.com.ua.KNUSchedule.Util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import geek.owl.com.ua.KNUSchedule.AppClass
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_GROUP
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_WEEK
import org.threeten.bp.LocalTime
import java.util.*

class NotificationWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val database = AppClass.database.getScheduleDao()
    private val notificationId= "111"
    lateinit var item:SchedulePojo

    override fun doWork(): Result {
        if (isTimeToShowNotification()) {
            if(item!=null) {
                showNotification()
            }
        }
        return Result.SUCCESS
    }

    private fun showNotification() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = notificationId
                val descriptionText = notificationId
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(notificationId, name, importance).apply {
                    description = descriptionText
                }

                // Register the channel with the system
                val notificationManager: NotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        val notification = NotificationCompat.Builder(context, notificationId)
                .setContentTitle(context.getString(R.string.lesson_start_soon))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_runer)
                .setContentText("${item.discipline}\n${item.beginTime}\nауд.${item.room} корп.${item.corps}\n${item.teachers}")
                .setStyle(NotificationCompat.BigTextStyle())
                .build()
        NotificationManagerCompat.from(context).notify(111, notification)
    }

    private fun isTimeToShowNotification(): Boolean {
   val curWeek = prefs.getInt(CURRENT_WEEK, -1)
            val curDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
            val curGroup:String = prefs.getString(CURRENT_GROUP, " ")!!
            val time = LocalTime.now()
            val nearestLesson = database.getSchedule(curGroup, curWeek, curDay, time)
        return if(nearestLesson.isNotEmpty()){
            item  = nearestLesson[0]
            item.start.plusMinutes(30L).isAfter(time)
        }else false
    }

}