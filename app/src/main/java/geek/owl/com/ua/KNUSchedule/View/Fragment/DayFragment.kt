package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.analytics.FirebaseAnalytics
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.Result
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleQuery
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.PaginationScrollListener
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.NotificationWorker
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_DAY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_FACULTY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_GROUP
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_WEEK
import geek.owl.com.ua.KNUSchedule.Util.WeekChecker
import geek.owl.com.ua.KNUSchedule.ViewModel.DayViewModel.DayViewModel
import kotlinx.android.synthetic.main.day_fragment_layout.*
import java.util.concurrent.TimeUnit

class DayFragment : androidx.fragment.app.Fragment(), OnItemClick {
  lateinit var firebase:FirebaseAnalytics
  private lateinit var adapter: SimpleAdapter

  lateinit var dayViewModel: DayViewModel
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    return inflater.inflate(R.layout.day_fragment_layout, container, false)
  }

  private val dayNumber: Int
      get() {
        return arguments?.getInt(CURRENT_DAY)!!
      }

  private val groupName: String
      get() {
        return arguments?.getString(CURRENT_GROUP)!!
      }

  private val weekNumber: Int
      get() {
        return arguments?.getInt(CURRENT_WEEK)!!
      }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()
    day_toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
    day_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    dayViewModel = ViewModelProviders.of(this).get(DayViewModel::class.java)
    dayViewModel.repo.queryData.postValue(ScheduleQuery( groupName,weekNumber, dayNumber,1))
    saveSelectedSettings()
    val weekChangeWork = PeriodicWorkRequest.Builder(WeekChecker::class.java, 2L, TimeUnit.HOURS).build()
    val notificationWorker = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1L, TimeUnit.MINUTES).build()
    WorkManager.getInstance().enqueue(listOf(notificationWorker, weekChangeWork))
    subscribeForData()
  }

  private fun saveSelectedSettings() {
    PreferenceManager.getDefaultSharedPreferences(this.context).edit().putInt(CURRENT_WEEK, weekNumber)
            .putString(CURRENT_GROUP, groupName)
            .putLong(CURRENT_FACULTY, arguments!![CURRENT_FACULTY] as Long).apply()
  }


  private fun setupRecyclerView() {
    adapter = SimpleAdapter(emptyList<SchedulePojo>().toMutableList(), this)
    day_recycler.layoutManager = LinearLayoutManager(this@DayFragment.context)
    day_recycler.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

    day_recycler.adapter = adapter
    day_recycler.addOnScrollListener(object : PaginationScrollListener(day_recycler.layoutManager as RecyclerView.LayoutManager){
      override fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView) {
        dayViewModel.repo.queryData.postValue(ScheduleQuery( groupName,weekNumber,dayNumber ,page))
      }
    })
  }

  private fun subscribeForData() {
    dayViewModel.liveData.observe(this, Observer {
      if (it != null){
        when(it){
          is Result.Success -> {
            if (it.data.isNotEmpty()) {
              adapter.data = it.data.toMutableList()
              adapter.notifyDataSetChanged()
            }
          }
          is Result.Error ->{  }
          is Result.Loading->{}
        }
      }
    })
  }

  override fun onClick(item: SimpleAdapter.ItemModel) {

  }


}