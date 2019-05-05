package geek.owl.com.ua.KNUSchedule.Util.Network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
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
          .baseUrl("https://otherstaff.xyz/knu_schedule/api/")
          .addCallAdapterFactory(CoroutineCallAdapterFactory())
          .addConverterFactory(GsonConverterFactory.create())
          .build()

      return retrofit.create(WebApi::class.java)
    }
  }
}

internal class LoginInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
        .addHeader("Username", "android")
        .addHeader("Authorization", "Token b797b453b861ca13b4f7066c17dffec625befec8")
        .build()

    return chain.proceed(request)
  }
}