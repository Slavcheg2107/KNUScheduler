package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.WeekPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.DayView
import geek.owl.com.ua.KNUSchedule.Util.getDayTitle
import geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel.ScheduleViewModel
import kotlinx.android.synthetic.main.week_fragment.*

class WeekFragment : androidx.fragment.app.Fragment(), OnItemClick {


  lateinit var viewModel: ScheduleViewModel
  val handler: Handler = Handler()
//  private val runnable = Runnable { refresh_layout?.isRefreshing = true }
  val week1Adapter:SimpleAdapter = SimpleAdapter(emptyList<WeekPojo>().toMutableList(),this)
  val week2Adapter:SimpleAdapter = SimpleAdapter(emptyList<WeekPojo>().toMutableList(),this)

  private val group: String
    get() {
      return arguments?.getString("group_name") as String
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
//   refresh_layout.setOnRefreshListener {
//     refresh_layout.isRefreshing = false
//      startDelayedLoad()
//      viewModel.getScheduleWithWeek(group, AppSettings().getInt(WEEK_NUMBER, 1))
//    }
  }

  private fun startDelayedLoad() {
//    handler.postDelayed(runnable, 500)
  }

  private fun cancelDelayedLoad() {
//    handler.removeCallbacksAndMessages(null)
  }


  private fun initWeekView() {
    week1.columnCount = 4
    week1.rowCount = 2
    week2.columnCount = 4
    week2.rowCount = 2
  }

  private fun subscribeForData() {
    viewModel.scheduleRepo.dayListLiveData.observe(this, Observer { it ->
      it?.let {
        cancelDelayedLoad()
        setData(it)
      }
    })
    viewModel.action.observe(this, Observer { it ->
      run {
        cancelDelayedLoad()
      }
    })
  }


  private fun setData(weeks: List<WeekPojo>) {
    weeks.forEach { week->
      when (week.weekNumber) {
        1 -> {
          week.list.forEach { day->
            run {
              week1.addView(this.context?.let { it1 ->
                DayView(it1).also {
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
          week.list.forEach { day->
            run {
              week2.addView(this.context?.let { it1 ->
                DayView(it1).also {
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

  override fun onClick(item: SimpleAdapter.ItemModel) {

  }
}