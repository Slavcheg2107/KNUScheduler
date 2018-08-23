package com.knu.krasn.knuscheduler.View.Fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.knu.krasn.knuscheduler.Repository.FacultyPojo
import com.knu.krasn.knuscheduler.Util.Action
import com.knu.krasn.knuscheduler.Util.Adapters.OnItemClick
import com.knu.krasn.knuscheduler.Util.Adapters.SimpleAdapter
import com.knu.krasn.knuscheduler.Util.KNUDiffUtil
import com.knu.krasn.knuscheduler.View.Activity.MainActivity
import com.knu.krasn.knuscheduler.ViewModel.FacultyViewModel.FacultyViewModel
import geek.owl.com.ua.KNUSchedule.R

class FacultyFragment : Fragment(), OnItemClick {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FacultyViewModel
    private lateinit var facAdapter: SimpleAdapter
    private var refreshLayout: SwipeRefreshLayout? = null
    private var runnable = Runnable { refreshLayout?.isRefreshing = true }
    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.faculty_fragment, container, false)
        refreshLayout = view.findViewById(R.id.refresh_layout)
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
//        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        initRecyclerView(view)
        viewModel = ViewModelProviders.of(this).get(FacultyViewModel::class.java)

        subscribeForData()
        return view
    }

    private fun subscribeForData() {
        viewModel.faculties.observe(this, Observer { it ->
            cancelDelayedLoad()
            it?.let { setData(it) }
        })
        viewModel.actionLiveData.observe(this, Observer {
            when (it) {
                Action.ERROR -> showMessage(getString(R.string.no_connetction))
                Action.TIMEOUT -> showMessage(getString(R.string.cant_connect))
            }
            cancelDelayedLoad()
        })
    }

    private fun startDelayedLoad() {
        handler.postDelayed(runnable, 500)
    }

    private fun cancelDelayedLoad() {
        handler.removeCallbacks(runnable)
    }

    private fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    private fun setData(data: List<FacultyPojo>) {
        val diff = KNUDiffUtil(facAdapter.data.toList(), data)
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(diff, true)
        facAdapter.data = data.toMutableList()
        result.dispatchUpdatesTo(facAdapter)
    }

    private fun initRecyclerView(view: View) {
        facAdapter = SimpleAdapter(emptyList<SimpleAdapter.ItemModel>().toMutableList(), this)
        recyclerView = view.findViewById(R.id.recycler_view)

        refreshLayout?.setOnRefreshListener {
            refreshLayout?.isRefreshing = false
            startDelayedLoad()
            viewModel.updateFaculties()
        }
        val lm = GridLayoutManager(context, 2)
        recyclerView.layoutManager = lm
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        recyclerView.adapter = facAdapter
    }

    override fun onClick(item: SimpleAdapter.ItemModel) {
        item as FacultyPojo
        val mainActivity = activity as MainActivity
        val b = Bundle().also { it.putLong("faculty_id", item.id) }
        mainActivity.controller.navigate(R.id.action_facultyFragment_to_groupFragment, b)
    }
}