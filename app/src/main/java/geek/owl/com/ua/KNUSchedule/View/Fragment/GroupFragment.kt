package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.KNUDiffUtil
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.TIMEOUT
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.UNKNOWN_HOST
import geek.owl.com.ua.KNUSchedule.ViewModel.GroupViewModel.GroupViewModel
import kotlinx.android.synthetic.main.group_fragment.view.*

class GroupFragment : BaseFragment(), OnItemClick {
  private lateinit var viewModel: GroupViewModel
  private lateinit var groupAdapter: SimpleAdapter
  private var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null
  private var runnable = Runnable { swipeRefreshLayout?.isRefreshing = true }
  private val handler = Handler()
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.group_fragment, container, false)
    initRecyclerView(view)
    initSearchView(view)
    swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    swipeRefreshLayout?.let {
      it.setOnRefreshListener {
        it.isRefreshing = false
        startDelayedLoad()
        viewModel.refresh()
      }
    }
    view.day_toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
    view.day_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
    subscribeForData()
    return view
  }


  private fun initSearchView(view: View?) {
    view?.search_view?.apply {
      this.setOnClickListener {
        expandSearchView(it)
      }
      this.setOnCloseListener {
        groupAdapter.isFiltering = false
        false
      }

      this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
          p0?.let { it ->
            groupAdapter.filterGroups(it)
          }
          return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {

          p0?.let { it ->
            groupAdapter.filterGroups(it)
          }
          return true
        }

      })
    }

  }

  private fun startDelayedLoad() {
    handler.postDelayed(runnable, 500)

  }

  private fun cancelDelayedLoad() {
    handler.removeCallbacks(runnable)
  }

  private fun expandSearchView(it: View?) {
    it as SearchView
    it.isIconified = false
    it.setQuery("", false)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      TransitionManager.beginDelayedTransition(it)
    }
  }

  private fun subscribeForData() {
    viewModel.getGroupLiveData(arguments?.get("facultyId") as Long).observe(this, Observer { list ->
      list?.let { setData(it) }
      cancelDelayedLoad()
    })
    viewModel.actionLiveData.observe(this, Observer {
      when (it) {
        UNKNOWN_HOST -> showMessage(getString(R.string.no_connetction))
        TIMEOUT -> showMessage(getString(R.string.cant_connect))
      }
      cancelDelayedLoad()
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

    val lm = androidx.recyclerview.widget.GridLayoutManager(context, 2)
    view.recycler_view.layoutManager = lm
    view.recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
    view.recycler_view.adapter = groupAdapter
  }

  override fun onClick(item: SimpleAdapter.ItemModel) {
    item as GroupPojo

    findNavController().navigate(R.id.action_groupFragment_to_weekFragment, Bundle().apply {
      this.putString("groupName", item.name)
      this.putLong("facultyId", item.facultyId.toLong())
    })
  }
}