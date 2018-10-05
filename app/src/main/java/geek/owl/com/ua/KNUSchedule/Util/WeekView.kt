package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import geek.owl.com.ua.KNUSchedule.R

class WeekView(context: Context?, attributeSet: AttributeSet?) : GridLayout(context, attributeSet) {

  var dayList = emptyList<DayView>()

  init {
    orientation = HORIZONTAL
    inflate(context, R.layout.week_item, null)
  }

  fun hideDay(day: WeekDays) {

  }

  fun showDay(day: WeekDays) {

  }

}