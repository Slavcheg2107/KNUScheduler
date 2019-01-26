package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
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
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_DAY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_FACULTY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_GROUP
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_WEEK
import geek.owl.com.ua.KNUSchedule.Util.getDayTitle
import geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel.ScheduleViewModel
import kotlinx.android.synthetic.main.week_fragment.*
import java.util.*

class WeekFragment : androidx.fragment.app.Fragment(), OnDayClick {


    lateinit var viewModel: ScheduleViewModel

    private val group: String
        get() {
            return arguments?.getString(CURRENT_GROUP) as String
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.week_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        day_toolbar.subtitle = group

        subscribeForData()
        viewModel.scheduleRepo.group.value = group

    }


    private fun init() {
        settings.setOnClickListener {
            findNavController().navigate(R.id.action_weekFragment_to_settingsFragment)
        }
        search_button.setOnClickListener {
            findNavController().navigate(R.id.action_weekFragment_to_searchFragment)
        }
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

        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1
        val currentWeek = PreferenceManager.getDefaultSharedPreferences(this.context).getInt(CURRENT_WEEK, -1)
        weeks.forEach { week ->

            when (week.weekNumber) {
                1 -> {
                    week.list.forEach { day ->
                        run {
                            val isToday = (day.number == dayOfWeek)&& (currentWeek == 1)
                            week1.addView(this.context?.let { it1 ->
                                DayView(it1, null, day, isToday, this@WeekFragment).also {
                                    it.title.text = (getDayTitle(day.number))
                                    it.setOnClickListener { onDayClick(day) }
                                    it.lessonCount.text = if (day.scheduleList.size == 1)
                                        "1 пара"
                                    else
                                        "${day.scheduleList.size} ${context?.resources?.getString(R.string.lesson)}"
                                    if (isToday) {
                                        it.mainLayout.background =
                                                resources.getDrawable(R.drawable.day_background_active)
                                        it.title.background = resources.getDrawable(R.drawable.text_view_background_active)
                                    } else {
                                        it.mainLayout.background =
                                                resources.getDrawable(R.drawable.day_background_inactive)
                                        it.title.background = resources.getDrawable(R.drawable.text_view_background_inactive)
                                    }
                                }
                            })
                        }
                    }
                }
                2 -> {
                    week.list.forEach { day ->
                        val isToday = (day.number == dayOfWeek)&& (currentWeek == 2)

                        run {
                            week2.addView(this.context?.let { it1 ->
                                DayView(it1, null, day, isToday, this@WeekFragment).also {
                                    it.title.text = (getDayTitle(day.number))
                                    it.setOnClickListener { onDayClick(day) }
                                    it.lessonCount.text = if (day.scheduleList.size == 1)
                                        "1 пара"
                                    else
                                        "${day.scheduleList.size} ${context?.resources?.getString(R.string.lesson)}"
                                    if (isToday) {
                                        it.mainLayout.background =
                                                resources.getDrawable(R.drawable.day_background_active)
                                        it.title.background = resources.getDrawable(R.drawable.text_view_background_active)
                                    } else {
                                        it.mainLayout.background =
                                                resources.getDrawable(R.drawable.day_background_inactive)
                                        it.title.background = resources.getDrawable(R.drawable.text_view_background_inactive)
                                    }
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
            this.putString(CURRENT_GROUP, group)
            arguments?.getLong(CURRENT_FACULTY)?.or(-1L)?.let { this.putLong(CURRENT_FACULTY, it) }
            this.putInt(CURRENT_WEEK, day.week)
            this.putInt(CURRENT_DAY, day.number)
        })
    }
}