package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import geek.owl.com.ua.KNUSchedule.R

class ScheduleSearchFragment : androidx.fragment.app.Fragment() {

  var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.list_fragment, container, false)
    initRecyclerView()

    return view
  }

  private fun initRecyclerView() {
    recyclerView = view!!.findViewById(R.id.recycler_view)
    val lm = androidx.recyclerview.widget.GridLayoutManager(context, 2)
    lm.isItemPrefetchEnabled
  }
}