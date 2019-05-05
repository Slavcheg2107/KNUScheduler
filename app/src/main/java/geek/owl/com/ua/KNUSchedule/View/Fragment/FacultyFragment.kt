package geek.owl.com.ua.KNUSchedule.View.Fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics

import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Repository.FacultyPojo
import geek.owl.com.ua.KNUSchedule.Util.Adapters.OnItemClick
import geek.owl.com.ua.KNUSchedule.Util.Adapters.SimpleAdapter
import geek.owl.com.ua.KNUSchedule.Util.KNUDiffUtil
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_FACULTY
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.CURRENT_WEEK
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.ERROR
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.TIMEOUT
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.UNKNOWN_HOST
import geek.owl.com.ua.KNUSchedule.ViewModel.FacultyViewModel.FacultyViewModel
import geek.owl.com.ua.KNUSchedule.mFirebaseAnalytics



class FacultyFragment : androidx.fragment.app.Fragment(), OnItemClick {
  private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
  private lateinit var viewModel: FacultyViewModel
  private lateinit var facAdapter: SimpleAdapter
  private var refreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


    return inflater.inflate(R.layout.faculty_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRecyclerView(view)
    viewModel = ViewModelProviders.of(this).get(FacultyViewModel::class.java)
    showSelectWeekDialog()
    subscribeForData()
  }
  private fun showSelectWeekDialog() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
    if(prefs.getInt(CURRENT_WEEK, -1)==-1) {
      val list:List<String> = ArrayList<String>().also {
        it.add("${getString(R.string.week)} 1")
        it.add("${getString(R.string.week)} 2")
      }

      val dialog = AlertDialog.Builder(this.context!!)
              .setTitle(getString(R.string.selectCurrentWeek))
              .setItems(list.toTypedArray()) { dialog, which ->
                prefs.edit().putInt(CURRENT_WEEK, which+1).apply()

              }.setPositiveButton("Ok") { dialog, which ->
              }.setCancelable(false)
              .create()
      dialog.show()
    }
  }

  private fun subscribeForData() {
    viewModel.faculties.observe(this, Observer { it ->
      it?.let { setData(it) }
    })
    viewModel.actionLiveData.observe(this, Observer {
      when (it) {
        ERROR-> showMessage(getString(R.string.no_connetction))
        TIMEOUT, UNKNOWN_HOST  -> showMessage(getString(R.string.cant_connect))
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
    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, Bundle()
            .also { it.putString("did_pick_faculty", item.name) })

    val b = Bundle().also { it.putLong(CURRENT_FACULTY, item.id) }
   findNavController().navigate(R.id.action_facultyFragment_to_groupFragment, b)
  }
}