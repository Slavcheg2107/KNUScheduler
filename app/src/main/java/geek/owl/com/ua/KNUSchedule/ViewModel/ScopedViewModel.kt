package geek.owl.com.ua.KNUSchedule.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext

open class ScopedViewModel : ViewModel(), CoroutineScope {

  private val job = Job()

  private val scope: CoroutineContext = job + Dispatchers.Main
  override val coroutineContext: CoroutineContext
    get() = scope
  override fun onCleared() {
    super.onCleared()
    job.cancel()
  }

}

