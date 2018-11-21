package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import geek.owl.com.ua.KNUSchedule.R
import geek.owl.com.ua.KNUSchedule.Util.WeekDays
import geek.owl.com.ua.KNUSchedule.Util.getDayTitle

class DayFragment : androidx.fragment.app.Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.list_fragment, container, false)

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val dayTitle = arguments?.getInt("dayNumber")?.let { getDayTitle(it) }

  }
}