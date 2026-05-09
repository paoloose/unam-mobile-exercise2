package site.paoloose.unam.exercise1.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var baseUrl = "https://www.computomovil.com/2026-2/"

    fun setBaseUrl(url: String) {
        baseUrl = url
        // In a real app, you'd recreate the retrofit instance if you change the base URL at runtime.
        // For standard use cases, setting it before first use is enough.
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val worldCupApiService: WorldCupApiService by lazy {
        retrofit.create(WorldCupApiService::class.java)
    }
}
