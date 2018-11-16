package geek.owl.com.ua.KNUSchedule.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext

open class ScopedViewModel : ViewModel(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = scope
  private val job = Job()

  protected val scope: CoroutineContext = job + Dispatchers.Main
  override fun onCleared() {
    super.onCleared()
    job.cancel()
  }

}

