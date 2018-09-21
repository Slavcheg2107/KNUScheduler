package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler

import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadLayoutAnimation

import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.DayPojo
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.AppSettings
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.WEEK_NUMBER
import geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel.ScheduleViewModel
import kotlinx.android.synthetic.main.week_fragment.*

class WeekFragment : Fragment(), OnItemClick {


    lateinit var viewModel: ScheduleViewModel
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    val handler: Handler = Handler()
    private val runnable = Runnable { swipeRefreshLayout?.isRefreshing = true }
    lateinit var weekAdapter: SimpleAdapter
    private val group: String
        get() {

            return arguments?.getString("group_name") as String
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.week_fragment, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        val groupName: String = group
        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        toolbar?.subtitle = groupName
        swipeRefreshLayout?.setOnRefreshListener {
            swipeRefreshLayout?.isRefreshing = false
            startDelayedLoad()
            viewModel.getSchedule(group, AppSettings().getInt(WEEK_NUMBER, 0))
        }
        initRecyclerView()
        subscribeForData()
        return view
    }

    private fun startDelayedLoad() {
        handler.postDelayed(runnable, 500)
    }

    private fun cancelDelayedLoad() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun initRecyclerView() {
        weekAdapter = SimpleAdapter(emptyArray<SimpleAdapter.ItemModel>().toMutableList(), this)
        val layoutManager = GridLayoutManager(context, 3)
        recycler_view?.adapter = weekAdapter
        recycler_view?.layoutManager = layoutManager
        recycler_view?.layoutAnimation = loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

    }

    private fun subscribeForData() {
        viewModel.getSchedule(group, AppSettings().getInt(WEEK_NUMBER, 1))
                .observe(this, Observer { it ->
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


    private fun setData(it: List<DayPojo> = emptyList()) {
            weekAdapter.data = it as MutableList<SimpleAdapter.ItemModel>
            weekAdapter.notifyDataSetChanged()
    }

    override fun onClick(item: SimpleAdapter.ItemModel) {

    }
}