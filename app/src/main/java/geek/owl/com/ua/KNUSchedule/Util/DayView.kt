package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.gridlayout.widget.GridLayout
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnDayClick


class DayView : FrameLayout {
  private lateinit var onItemClick: OnDayClick
  lateinit var day: DayPojo

  constructor(context: Context, attributeSet: AttributeSet?, day: DayPojo, isToday: Boolean) : super(context, attributeSet) {
    this.day = day
    this.isToday = isToday
  }
  var mainLayout : FrameLayout
  var title: TextView
  var lessonCount: TextView
  var isToday: Boolean = false

  constructor(context: Context, day: DayPojo, onDayClick: OnDayClick) : super(context) {
    this.day = day
    this.onItemClick = onDayClick
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

  init {
    inflate(context, R.layout.day_item, this as ViewGroup).also {
      val param = GridLayout.LayoutParams(GridLayout.spec(
          GridLayout.UNDEFINED, GridLayout.CENTER, 1f),
          GridLayout.spec(GridLayout.UNDEFINED, GridLayout.CENTER, 1f))
      it.layoutParams = param
    }

    mainLayout = findViewById(R.id.day_main_layout)
    title = findViewById(R.id.day_of_week)
    lessonCount = findViewById(R.id.num_of_lessons)
    val attributes = context.obtainStyledAttributes(R.styleable.DayView)
    mainLayout.background = if(isToday)resources.getDrawable(R.drawable.day_background_active)else resources.getDrawable(R.drawable.day_background_active)
    title.background = if(isToday)resources.getDrawable(R.drawable.text_view_background_active) else resources.getDrawable(R.drawable.text_view_background_inactive)
    title.text = attributes?.getString(R.styleable.DayView_title)
    lessonCount.text = attributes?.getString(R.styleable.DayView_lesson_count)
    attributes?.recycle()
  }


  override fun callOnClick(): Boolean {
    onItemClick.onDayClick(day)
    return super.callOnClick()
  }
}