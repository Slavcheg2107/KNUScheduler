package geek.owl.com.ua.KNUSchedule.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import geek.owl.com.ua.KNUSchedule.R

class DayFragment : androidx.fragment.app.Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    var view = inflater.inflate(R.layout.list_fragment, container, false)


    return view
  }
}