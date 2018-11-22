package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnDayClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import androidx.gridlayout.widget.GridLayout


class DayView : CardView {
private lateinit var onItemClick : OnDayClick
  constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

  private lateinit var day: DayPojo

  constructor(context: Context, day:DayPojo, onDayClick: OnDayClick) : super(context){
    this.day = day
    this.onItemClick = onDayClick
  }
  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

  var title: TextView
  var lessonCount: TextView
  init {
    inflate(context, R.layout.day_item, this as ViewGroup).also {
      val param = GridLayout.LayoutParams(GridLayout.spec(
          GridLayout.UNDEFINED, GridLayout.FILL, 1f),
          GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)).apply {
      }
    it.layoutParams = param}
    title = findViewById(R.id.day_of_week)
    lessonCount = findViewById(R.id.num_of_lessons)
    val attributes = context.obtainStyledAttributes( R.styleable.DayView)
    title.text = attributes?.getString(R.styleable.DayView_title)
    lessonCount.text = attributes?.getString(R.styleable.DayView_lesson_count)
    attributes?.recycle()
  }

  override fun callOnClick(): Boolean {
    onItemClick.onDayClick(day)
    return super.callOnClick()
  }
}