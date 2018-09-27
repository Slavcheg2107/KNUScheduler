package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import geek.owl.com.ua.KNUSchedule.R

class DayView (context: Context?, attributeSet: AttributeSet?): View(context, attributeSet){

    var title:TextView
    var lessonNumber : TextView
    init {
        inflate(context, R.layout.day_item, parent as ViewGroup)
        title = findViewById(R.id.title)
        lessonNumber = findViewById(R.id.num_of_lessons)
        val attributes = context?.obtainStyledAttributes(attributeSet, R.styleable.DayView)
        title.text = attributes?.getString(R.styleable.DayView_title)
        lessonNumber.text = attributes?.getString(R.styleable.DayView_lesson_number)

        attributes?.recycle()

    }
    fun setTitle(title: String) {
        this.title.text = title
    }

    fun setLessonsNumber(lessonNum: String) {
        this.lessonNumber.text = lessonNum
    }

}