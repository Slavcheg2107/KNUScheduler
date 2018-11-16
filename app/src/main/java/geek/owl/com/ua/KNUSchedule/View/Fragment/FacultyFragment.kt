package geek.owl.com.ua.KNUSchedule.View.Fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast

import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.KNUDiffUtil
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.ERROR
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.TIMEOUT
import geek.owl.com.ua.KNUSchedule.View.Activity.MainActivity
import geek.owl.com.ua.KNUSchedule.ViewModel.FacultyViewModel.FacultyViewModel

class FacultyFragment : androidx.fragment.app.Fragment(), OnItemClick {
  private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
  private lateinit var viewModel: FacultyViewModel
  private lateinit var facAdapter: SimpleAdapter
  private var refreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null
  private var runnable = Runnable { refreshLayout?.isRefreshing = true }
  private val handler = Handler()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.faculty_fragment, container, false)
    refreshLayout = view.findViewById(R.id.refresh_layout)
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
//        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    initRecyclerView(view)
    viewModel = ViewModelProviders.of(this).get(FacultyViewModel::class.java)
    refreshLayout?.setOnRefreshListener {
      refreshLayout?.isRefreshing = false
      viewModel.updateFaculties()
    }
    subscribeForData()
    return view
  }

  private fun subscribeForData() {
    viewModel.faculties.observe(this, Observer { it ->
      it?.let { setData(it) }
    })
    viewModel.actionLiveData.observe(this, Observer {
      when (it) {
        ERROR -> showMessage(getString(R.string.no_connetction))
        TIMEOUT -> showMessage(getString(R.string.cant_connect))
      }
    })
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

    val lm = androidx.recyclerview.widget.GridLayoutManager(context, 2)
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