package geek.owl.com.ua.KNUSchedule.Repository.Network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal class RetrofitConfig {

    private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)


    private val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(LoginInterceptor())
            .connectTimeout(4000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()

    private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .baseUrl(BASE_URL)
            .build()

    val apiNetwork = retrofit.create(ApiInterface::class.java)

    companion object {
        const val BASE_URL = "https://otherstaff.xyz/api/"
    }

    class LoginInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            request.let {
                it.newBuilder()?.apply {
                    addHeader("Authorization", "Token 8fd6e8ac1137f7b5c0d9604a923cb04792994343").addHeader("Username", "android")
                }
            }
            return chain.proceed(request)
        }
    }
}