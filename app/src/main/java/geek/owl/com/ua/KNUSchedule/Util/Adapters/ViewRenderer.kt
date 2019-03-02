package geek.owl.com.ua.KNUSchedule.Util.Adapters

import android.app.AlertDialog
import android.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import geek.owl.com.ua.KNUSchedule.Repository.*
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_WEEK
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.NOTIFICATION
import android.R.attr.label
import androidx.core.content.ContextCompat.getSystemService
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import geek.owl.com.ua.KNUSchedule.AppClass
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_FACULTY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_GROUP
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.Temporal
import org.threeten.bp.temporal.TemporalUnit
import java.util.*


class ViewRenderer {
    fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.faculty_item, parent, false)
        return when (viewType) {
            ItemType.FACULTY.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.faculty_item, parent, false)
                FacultyViewHolder(view)
            }
            ItemType.GROUP.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
                GroupViewHolder(view)
            }
            ItemType.WEEK.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.week_item, parent, false)
                WeekViewHolder(view)
            }
            ItemType.DAY.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
                DayViewHolder(view)
            }
            ItemType.SCHEDULE.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
                ScheduleViewHolder(view)
            }
            ItemType.GROUP_SETTING.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.group_settings_item, parent, false)
                GroupSettingViewHolder(view)
            }
            ItemType.LIST_SETTING.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_settings_item, parent, false)
                ListSettingViewHolder(view)
            }
            ItemType.LINK_SETTING.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.link_settings_item, parent, false)
                LinkSettingViewHolder(view)
            }
            ItemType.SWITCH_SETTING.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.switch_settings_item, parent, false)
                SwitchSettingViewHolder(view)
            }
            ItemType.WALLET_SETTING.ordinal -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.wallet_item, parent, false)
                MoneyViewHolder(view)
            }
            else -> {

                FacultyViewHolder(view)
            }
        }

    }

    inner class FacultyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView? = view.findViewById(R.id.title)

        fun bind(item: FacultyPojo, itemClickListener: OnItemClick) {
            title?.text = item.name
            itemView.setOnClickListener { itemClickListener.onClick(item) }
        }
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(R.id.title)
        fun bind(item: GroupPojo, itemClickListener: OnItemClick) {
            title?.text = item.name
            itemView.setOnClickListener { itemClickListener.onClick(item) }
        }
    }

    inner class WeekViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView? = view.findViewById(R.id.title)

        fun bind(title: String, itemClickListener: OnItemClick) {
            this.title?.text = title
        }
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: DayPojo, itemClickListener: OnItemClick) {

        }
    }

    inner class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val lessonTitle: TextView = view.findViewById(R.id.lessonTitle)
        private val lessonTime: TextView = view.findViewById(R.id.lessonTime)
        private val startIn:TextView = view.findViewById(R.id.startIn)
        private val lessonPlace: TextView = view.findViewById(R.id.lessonPlace)
        private val lessonTeachers: TextView = view.findViewById(R.id.lessonTeachers)
        private val shareButton:ImageView = view.findViewById(R.id.share)
        private val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)

        val lecture = itemView.context.getString(R.string.lecture)
        private val practic = itemView.context.getString(R.string.practic)
        fun bind(item: SchedulePojo, itemClickListener: OnItemClick) {
            lessonTime.text = String.format("${item.lessonNumber} ${itemView.context.getString(R.string.lesson)} ${if(item.lessontype=="лекція")lecture else practic} (${item.beginTime} - ${item.endTime})")
            lessonTitle.text = item.discipline
            lessonTeachers.text = item.teachers
            lessonPlace.text = String.format("в ${item.corps} корп. ${item.room} ауд.")
            val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

            if(day == item.day && item.week == prefs.getInt(CURRENT_WEEK, -1)){
            val time = LocalTime.now().minusMinutes(item.start.minute.toLong()).minusHours(item.start.hour.toLong())
            startIn.text = "${itemView.context.getString(R.string.start_in)} ${time.format(DateTimeFormatter.ofPattern("hh:mm"))}"
            }else startIn.text = ""
            shareButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val text = "${item.discipline}\n${item.beginTime} - ${item.endTime}\nауд. ${item.room}\n${item.teachers}\n(здесь будет ссылка на приложение)"
                intent.putExtra(Intent.EXTRA_TEXT,text)
                AppClass.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, Bundle()
                        .also { it.putString("AnalyticsEventShare", text) })
                itemView.context.startActivity(Intent.createChooser(intent, "Share via"))
            }
        }
    }

    inner class GroupSettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: GroupSetting, settingsClickListener: OnItemClick) {

        }

    }

    inner class LinkSettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val linkSettingTitle: TextView = view.findViewById(R.id.link_settings_title)
        private val icon:ImageView = view.findViewById(R.id.imageView3)
        private val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)
        fun bind(item: LinkSetting, settingsClickListener: OnItemClick) {
            linkSettingTitle.text = item.title
            icon.setImageDrawable(if(item.title == itemView.context.getString(R.string.group_preference_title)) itemView.context.getDrawable(R.drawable.new_schedule) else itemView.context.getDrawable(R.drawable.refresh))
            itemView.setOnClickListener {
                prefs.edit().remove(CURRENT_GROUP).remove(CURRENT_FACULTY).apply()
                settingsClickListener.onClick(item)
            }
        }
    }

    inner class ListSettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val weekTitle: TextView = view.findViewById(R.id.week_title)
        val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)

        fun bind(item: ListSetting, settingsClickListener: OnItemClick) {
            prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                if(key == CURRENT_WEEK) {
                    val value = sharedPreferences.getInt(key, -1)
                    weekTitle.text = if(value>-1) "${itemView.context.getString(R.string.week)} $value" else ""
                    }
                }
            title.text = item.title
            val value = prefs
                    .getInt(CURRENT_WEEK, -1)
            weekTitle.text = if(value>-1) "${itemView.context.getString(R.string.week)} $value" else ""

            itemView.setOnClickListener {
                showSelectWeekDialog(prefs, itemView.context)
            }
        }
    }

    private fun showSelectWeekDialog(prefs: SharedPreferences, context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(context.getString(R.string.selectCurrentWeek))
                .setItems(context.resources.getStringArray(R.array.week_list)) { dialog, which ->
                    prefs.edit().putInt(CURRENT_WEEK, which+1).apply() }
                .setPositiveButton("OK") { dialog, which ->  }
                .create().show()

    }


    inner class SwitchSettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.swithc_title)
        val icon:ImageView = view.findViewById(R.id.imageView)
        private val switch: Switch = view.findViewById(R.id.switch1)
        val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)

        fun bind(item: SwitchSetting, settingsClickListener: OnItemClick) {
            prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                if(key == NOTIFICATION)
                prefs.getBoolean(key, true)
            }
            icon.setImageDrawable(this.itemView.resources.getDrawable(R.drawable.ic_notification, null))
            title.text = item.title
            switch.isChecked = prefs.getBoolean(NOTIFICATION, true)
            switch.setOnCheckedChangeListener { buttonView, isChecked ->
                prefs.edit().putBoolean(NOTIFICATION, isChecked).apply()
            }
        }

    }

    inner class MoneyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(geek.owl.com.ua.KNUSchedule.R.id.title)
        val subTitle: TextView = view.findViewById(geek.owl.com.ua.KNUSchedule.R.id.subtitle)

        fun bind(wallet: Wallet, settingsClickListener: OnItemClick) {
            title.text = wallet.title
            subTitle.text = wallet.subtitle
            itemView.setOnClickListener {
                val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Copied Text", subTitle.text)
                clipboard.primaryClip = clip
                Toast.makeText(itemView.context, "Copied to buffer", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


