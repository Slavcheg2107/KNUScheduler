package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import geek.owl.com.ua.KNUSchedule.R

class WeekView(context: Context?, attributeSet: AttributeSet?) : GridLayout(context, attributeSet) {

    var dayList = emptyList<DayView>()
    init {
        orientation = HORIZONTAL
        inflate(context, R.layout.week_item, null)
    }

    fun hideDay(day : WeekDays){

    }
    fun showDay(day : WeekDays){

    }

}