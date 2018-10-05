package geek.owl.com.ua.KNUSchedule.Util.Network


data class Result<out T>(val success: T? = null, val error: Throwable? = null)