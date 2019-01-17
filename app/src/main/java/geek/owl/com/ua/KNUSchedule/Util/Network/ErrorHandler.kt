package geek.owl.com.ua.KNUSchedule.Util.Network


import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.ERROR
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.TIMEOUT
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.UNKNOWN
import geek.owl.com.ua.KNUSchedule.Util.StaticVariables.Companion.UNKNOWN_HOST
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


    fun Exception.getMessage(): String {
      return when (this) {
        is HttpException -> ERROR
        is UnknownHostException -> UNKNOWN_HOST
        is SocketTimeoutException -> TIMEOUT
        else -> UNKNOWN
      }
    }
