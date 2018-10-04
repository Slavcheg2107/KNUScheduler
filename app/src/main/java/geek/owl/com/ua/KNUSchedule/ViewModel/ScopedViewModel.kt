package geek.owl.com.ua.KNUSchedule.ViewModel

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main

open class ScopedViewModel : ViewModel() {
    private val job = Job()

    protected val scope: CoroutineScope = CoroutineScope(job + Dispatchers.Main)
    override fun onCleared(){
        super.onCleared()
            job.cancel()
    }

}

