package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.WeekPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnDayClick
import geek.owl.com.ua.KNUSchedule.Util.DayView
import geek.owl.com.ua.KNUSchedule.Util.getDayTitle
import geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel.ScheduleViewModel
import kotlinx.android.synthetic.main.week_fragment.*
import java.util.*

class WeekFragment : androidx.fragment.app.Fragment(), OnDayClick {


  lateinit var viewModel: ScheduleViewModel

  private val group: String
    get() {
      return arguments?.getString("groupName") as String
    }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.week_fragment, container, false)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initWeekView()

    viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    toolbar?.subtitle = group

    subscribeForData()
    viewModel.scheduleRepo.group.value = group

  }


  private fun initWeekView() {

  }

  private fun subscribeForData() {
    viewModel.scheduleRepo.dayListLiveData.observe(this, Observer { it ->
      it?.let {
        setData(it)
      }
    })
    viewModel.action.observe(this, Observer { it ->
      run {
      }
    })
  }


  private fun setData(weeks: List<WeekPojo>) {
    week1.removeAllViews()
    week2.removeAllViews()
    weeks.forEach { week ->

      when (week.weekNumber) {
        1 -> {
          week.list.forEach { day ->
            run {
              week1.addView(this.context?.let { it1 ->
                DayView(it1, day, this@WeekFragment, day.number == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).also {
                  it.title.text = (getDayTitle(day.number))
                  it.lessonCount.text = if (day.scheduleList.size == 1)
                    "1 пара"
                  else
                    "${day.scheduleList.size} ${context?.resources?.getString(R.string.lesson)}"

                }
              })
            }
          }
        }
        2 -> {
          week.list.forEach { day ->
            run {
              week2.addView(this.context?.let { it1 ->
                DayView(it1, day, this@WeekFragment, day.number == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).also {
                  it.title.text = (getDayTitle(day.number))
                  it.lessonCount.text = if (day.scheduleList.size == 1)
                    "1 пара"
                  else
                    "${day.scheduleList.size} ${context?.resources?.getString(R.string.lesson)}"

                }
              })
            }
          }
        }
      }
    }
  }

  override fun onDayClick(day: DayPojo) {

    findNavController().navigate(R.id.action_weekFragment_to_dayFragment, Bundle().apply {
      this.putString("groupName", group)
      arguments?.getLong("facultyId")?.or(-1L)?.let { this.putLong("facultyId", it) }
      this.putLong("weekNumber", day.weekNumber.toLong())
      this.putInt("DayNumber", day.number)
    })
  }
}