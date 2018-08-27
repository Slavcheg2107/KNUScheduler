package geek.owl.com.ua.KNUSchedule.Util

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.owl.krasn.KNUSchedule.Util.Network.WebApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class ApiService {
    companion object {
        fun createApi(): WebApi {
            val loggingInterceptor = HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(LoginInterceptor())
                    .retryOnConnectionFailure(true)
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl("https://immense-refuge-24759.herokuapp.com/api/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(WebApi::class.java)
        }
    }
}

internal class LoginInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .addHeader("Username", "android")
                .addHeader("Authorization", "Token 6369aebbfac97f6c2ae09f0b6cdde90d4cfa0718")
                .build()

        return chain.proceed(request)
    }
}