package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.SchedulePojo
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleQuery
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.PaginationScrollListener
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.ViewModel.ScheduleViewModel.SearchViewModel
import kotlinx.android.synthetic.main.day_fragment_layout.*
import kotlinx.android.synthetic.main.search_fragment_layout.*

class SearchFragment : BaseFragment(), OnItemClick {
    override fun onClick(item: SimpleAdapter.ItemModel) {

    }

    private var page: Int = 1

    lateinit var adapter: SimpleAdapter
    lateinit var searchView: SearchView
    val searchViewModel = SearchViewModel()

    var query: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_toolbar.inflateMenu(R.menu.search_menu)
        setupRecyclerView()
        setupSearch(search_toolbar)
        subscribeForData()
    }

    private fun subscribeForData() {
        searchViewModel.repo.searchResult.observe(this, Observer {
            setData(it)
        })
        searchViewModel.action.observe(this, Observer {

        })
    }

    private fun setData(it: List<SchedulePojo>) {
        adapter.data = it.toMutableList()
        adapter.notifyDataSetChanged()
    }


    private fun setupRecyclerView() {
        adapter = SimpleAdapter(emptyList<SchedulePojo>().toMutableList(), this)
        search_recycler.layoutManager = LinearLayoutManager(this.context)
        search_recycler.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        search_recycler.adapter = adapter
        search_recycler.addOnScrollListener(object : PaginationScrollListener(search_recycler.layoutManager as RecyclerView.LayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView) {
                this@SearchFragment.page = page
                searchViewModel.getSchedule(query, page)
            }
        })
    }

    private fun setupSearch(search_toolbar: Toolbar) {
        val menu = search_toolbar.menu
        searchView = menu.getItem(0).actionView as SearchView
        searchView.onActionViewExpanded()
        searchView.setQuery("", false)
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    query = newText
                }
                searchViewModel.getSchedule(query, page)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    this@SearchFragment.query = query
                }
                searchViewModel.getSchedule(this@SearchFragment.query, page)
                return false
            }
        })
    }
}