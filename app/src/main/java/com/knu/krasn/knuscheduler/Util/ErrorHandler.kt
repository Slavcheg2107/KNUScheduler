package com.knu.krasn.knuscheduler.Util

import com.knu.krasn.knuscheduler.Util.StaticVariables.Companion.ERROR
import com.knu.krasn.knuscheduler.Util.StaticVariables.Companion.TIMEOUT
import com.knu.krasn.knuscheduler.Util.StaticVariables.Companion.UNKNOWN
import com.knu.krasn.knuscheduler.Util.StaticVariables.Companion.UNKNOWN_HOST
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandler(){

    companion object {
        fun getMessage(e : Exception) : String{
            return when (e){
                is HttpException -> ERROR
                is UnknownHostException -> UNKNOWN_HOST
                is SocketTimeoutException -> TIMEOUT
                else -> UNKNOWN
            }
            }
        }
    }
