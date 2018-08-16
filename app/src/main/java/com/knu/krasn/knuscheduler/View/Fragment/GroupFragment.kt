package com.knu.krasn.knuscheduler.View.Fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.knu.krasn.knuscheduler.Repository.GroupPojo
import com.knu.krasn.knuscheduler.Util.Action
import com.knu.krasn.knuscheduler.Util.Adapters.OnItemClick
import com.knu.krasn.knuscheduler.Util.Adapters.SimpleAdapter
import com.knu.krasn.knuscheduler.Util.KNUDiffUtil
import com.knu.krasn.knuscheduler.ViewModel.GroupViewModel.GroupViewModel
import kotlinx.android.synthetic.main.group_fragment.view.*

class GroupFragment : Fragment(), OnItemClick {
    private lateinit var viewModel: GroupViewModel
    private lateinit var groupAdapter: SimpleAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.group_fragment, container, false)
        initRecyclerView(view)
        view.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        view.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        subscribeForData()
        return view
    }

    private fun subscribeForData() {
        var i = arguments?.get("faculty_id") as Long
        viewModel.getGroupLiveData(arguments?.get("faculty_id") as Long).observe(this, Observer { list ->
            list?.let { setData(it) }
        })
        viewModel.actionLiveData.observe(this, Observer {
            when (it) {
                Action.ERROR -> showMessage(getString(R.string.no_connetction))
                Action.TIMEOUT -> showMessage(getString(R.string.cant_connect))
            }
        })
    }

    private fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    private fun setData(data: List<GroupPojo>) {

        val diff = KNUDiffUtil(data, groupAdapter.data.toList())
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(diff, true)
        groupAdapter.data = data.toMutableList()
        result.dispatchUpdatesTo(groupAdapter)
    }

    private fun initRecyclerView(view: View) {
        groupAdapter = SimpleAdapter(emptyList<SimpleAdapter.ItemModel>().toMutableList(), this)

        val lm = GridLayoutManager(context, 2)
        view.recycler_view.layoutManager = lm
        view.recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        view.recycler_view.adapter = groupAdapter
    }

    override fun onClick(item: SimpleAdapter.ItemModel) {
        item as GroupPojo

    }
}