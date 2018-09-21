package geek.owl.com.ua.KNUSchedule.ViewModel

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

open class ScopedViewModel : ViewModel() {
    private val job = Job()

    protected val scope: CoroutineScope = CoroutineScope(job + Dispatchers.Main)
    override fun onCleared(){
        super.onCleared()
            job.cancel()
    }

}

