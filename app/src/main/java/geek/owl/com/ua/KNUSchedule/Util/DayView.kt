package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import geek.owl.com.ua.KNUSchedule.R

class DayView : CardView {
  fun setTitle(number: Int) {
    when (number){
      WeekDays.MONDAY.ordinal->{ title.text = WeekDays.MONDAY.name}
    }
  }

  constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
  constructor(context: Context) : super(context)
  constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

  var title: TextView
  var lessonCount: TextView
  init {
    inflate(context, R.layout.day_item, this as ViewGroup)
    title = findViewById(R.id.title)
    lessonCount = findViewById(R.id.num_of_lessons)
    val attributes = context.obtainStyledAttributes( R.styleable.DayView)
    title.text = attributes?.getString(R.styleable.DayView_title)
    lessonCount.text = attributes?.getString(R.styleable.DayView_lesson_count)
    attributes?.recycle()
  }

}