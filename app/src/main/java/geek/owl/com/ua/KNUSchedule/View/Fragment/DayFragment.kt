package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.Result
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleQuery
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.PaginationScrollListener
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.ViewModel.DayViewModel.DayViewModel
import kotlinx.android.synthetic.main.day_fragment_layout.*

class DayFragment : androidx.fragment.app.Fragment(), OnItemClick {

  private lateinit var adapter: SimpleAdapter

  lateinit var dayViewModel: DayViewModel
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    return inflater.inflate(R.layout.day_fragment_layout, container, false)
  }

  private val dayNumber: Int
      get() {
        return arguments?.getInt("dayNumber")!!
      }

  private val groupName: String
      get() {
        return arguments?.getString("groupName")!!
      }

  private val weekNumber: Int
      get() {
        return arguments?.getInt("weekNumber")!!
      }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()

    dayViewModel = ViewModelProviders.of(this).get(DayViewModel::class.java)
    dayViewModel.repo.queryData.postValue(ScheduleQuery( groupName,weekNumber, dayNumber,1))

    subscribeForData()
  }


  private fun setupRecyclerView() {
    adapter = SimpleAdapter(emptyList<SchedulePojo>().toMutableList(), this)
    day_recycler.layoutManager = LinearLayoutManager(this@DayFragment.context)
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