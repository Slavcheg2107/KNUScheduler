package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo

class WeekView(context: Context?) : GridLayout(context) {

  var dayViewList: MutableList<DayView> = emptyList<DayView>().toMutableList()
  var dayList: MutableList<DayPojo> = emptyList<DayPojo>().toMutableList()


  init {

  }


  fun addDays(days: List<DayPojo>) {
    days.forEach { day ->
      addView(DayView(context).also {
//        it.title.text = getDayTitle(day.number)
        it.lessonCount.text = if (day.scheduleList.size == 1)
          "1 пара"
        else
          "${day.scheduleList.size} ${context.resources.getString(R.string.lesson)}"
        dayViewList.add(it)
      })
    }
    dayList = days.toMutableList()
  }


  }

  private fun addDay(dayPojo: DayPojo) {
    val attributeSet: AttributeSet
//    addView()
  }

