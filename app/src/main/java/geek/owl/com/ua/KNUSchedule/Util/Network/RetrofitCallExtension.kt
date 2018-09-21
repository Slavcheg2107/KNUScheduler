package geek.owl.com.ua.KNUSchedule.Util.Network

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext


data class Result<out T>(val success: T? = null, val error: Throwable? = null)