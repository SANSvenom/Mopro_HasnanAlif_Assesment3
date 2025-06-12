package com.hasnan0062.assesment3.network

import com.hasnan0062.assesment3.model.Kucing
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.thecatapi.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface KucingApiService {
    @GET("images/search?limit=10")
    suspend fun getKucing(): List<Kucing>
}


object KucingApi {
    val service: KucingApiService by lazy {
        retrofit.create(KucingApiService::class.java)
    }

    fun getHewaUrl(imageId: String): String {
        return "$BASE_URL$imageId.jpg"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }