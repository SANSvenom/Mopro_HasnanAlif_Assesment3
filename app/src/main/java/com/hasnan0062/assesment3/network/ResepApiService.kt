package com.hasnan0062.assesment3.network

import com.hasnan0062.assesment3.model.Resep
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ResepApiService {
    @GET("/PixLib")
    suspend fun getResep(@Query("userId") userId: String): List<Resep>

    @GET("/PixLib")
    suspend fun getResepAll(): List<Resep>

    @DELETE("/PixLib/{id}")
    suspend fun deleteResep(
        @Path("id") id: String
    )

    @FormUrlEncoded
    @PUT("/PixLib/{id}")
    suspend fun updateResep(
        @Path("id") id: String,
        @Field("nama") nama: String,
        @Field("waktu") waktu: String,
        @Field("keterangan") keterangan: String,
        @Field("userId") userId: String,
        @Field("imageUrl") imageUrl: String
    ): Resep

    @FormUrlEncoded
    @POST("/PixLib")
    suspend fun postResep(
        @Field("nama") nama: String,
        @Field("waktu") waktu: String,
        @Field("keterangan") keterangan: String,
        @Field("userId") userId: String,
        @Field("imageUrl") imageUrl: String
    ): Resep
}

object ResepApi {
    private const val BASE_URL = "https://684ba29aed2578be881beeec.mockapi.io"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ResepApiService = retrofit.create(ResepApiService::class.java)
}


enum class ApiStatus { LOADING, SUCCES, FAILED }