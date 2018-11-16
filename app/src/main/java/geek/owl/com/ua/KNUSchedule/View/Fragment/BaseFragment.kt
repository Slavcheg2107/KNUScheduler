package geek.owl.com.ua.KNUSchedule.View.Fragment

import androidx.fragment.app.Fragment

open class BaseFragment : androidx.fragment.app.Fragment(), OnBackPressedListener {
  override fun onBackPressed(): Boolean {
    return true
  }
}