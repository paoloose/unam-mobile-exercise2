package site.paoloose.unam.exercise2.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val COMPUTOMOVIL_BASE_URL = "https://www.computomovil.com/2026-2/"
    private const val API_FOOTBALL_BASE_URL = "https://v3.football.api-sports.io/"
    private const val API_FOOTBALL_KEY = "b73712f52765da1fb71ba8b7d5d6413e"

    fun setBaseUrl(url: String) {
        computomovilBaseUrl = url
    }

    private var computomovilBaseUrl = COMPUTOMOVIL_BASE_URL

    private val footballApiInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("x-apisports-key", API_FOOTBALL_KEY)
            .build()
        chain.proceed(request)
    }

    private val footballOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(footballApiInterceptor)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(computomovilBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val footballRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_FOOTBALL_BASE_URL)
            .client(footballOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val worldCupApiService: WorldCupApiService by lazy {
        retrofit.create(WorldCupApiService::class.java)
    }

    val footballApiService: ApiFootballService by lazy {
        footballRetrofit.create(ApiFootballService::class.java)
    }
}
