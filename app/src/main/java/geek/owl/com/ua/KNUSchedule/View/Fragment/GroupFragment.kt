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
import com.google.firebase.analytics.FirebaseAnalytics
import geek.owl.com.ua.KNUSchedule.AppClass
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.GroupPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.KNUDiffUtil
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_FACULTY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_GROUP
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.TIMEOUT
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.UNKNOWN_HOST
import geek.owl.com.ua.KNUSchedule.ViewModel.GroupViewModel.GroupViewModel
import kotlinx.android.synthetic.main.group_fragment.*
import kotlinx.android.synthetic.main.group_fragment.view.*

class GroupFragment : BaseFragment(), OnItemClick {
    private lateinit var viewModel: GroupViewModel
    private lateinit var groupAdapter: SimpleAdapter
    private var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.group_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initSearchView(view)

        view.day_toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        view.day_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        subscribeForData()

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


    private fun expandSearchView(it: View?) {
        it as SearchView
        it.isIconified = false
        it.setQuery("", false)
        TransitionManager.beginDelayedTransition(it)
    }

    private fun subscribeForData() {
        viewModel.getGroupLiveData(arguments?.get(CURRENT_FACULTY) as Long).observe(this, Observer { list ->
            list?.let { setData(it) }
        })
        viewModel.actionLiveData.observe(this, Observer {
            when (it) {
                UNKNOWN_HOST -> showMessage(getString(R.string.no_connetction))
                TIMEOUT -> showMessage(getString(R.string.cant_connect))
            }
        })
    }

    private fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    private fun setData(data: List<GroupPojo>) {

        groupAdapter.data = data.toMutableList()
        groupAdapter.filterGroups("")
    }

    private fun initRecyclerView(view: View) {
        groupAdapter = SimpleAdapter(emptyList<SimpleAdapter.ItemModel>().toMutableList(), this)

        val lm = androidx.recyclerview.widget.GridLayoutManager(context, 2)
        recycler_view.layoutManager = lm
        recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        recycler_view.adapter = groupAdapter
    }

    override fun onClick(item: SimpleAdapter.ItemModel) {
        item as GroupPojo
        AppClass.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, Bundle()
                .also { it.putString("did_pick_group", item.name) })

        findNavController().navigate(R.id.action_groupFragment_to_weekFragment, Bundle().apply {
            this.putString(CURRENT_GROUP, item.name)
            this.putLong(CURRENT_FACULTY, item.facultyId.toLong())
        })
    }
}