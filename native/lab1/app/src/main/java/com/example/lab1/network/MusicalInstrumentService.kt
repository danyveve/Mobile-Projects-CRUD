package com.example.lab1.network

import com.example.lab1.MusicalInstrument
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


object MusicalInstrumentApi {
    //Running from the emulator, start the server first.
    private const val URL = "http://10.0.2.2:8080/"

    interface Service {
        @GET("/musicalInstrumentsApi/getAll")
        suspend fun getMusicalInstruments(): List<MusicalInstrument>

        @POST("/musicalInstrumentsApi/save")
        suspend fun saveMusicalInstrument(@Body musicalInstrument: MusicalInstrument): Long

        @PUT("/musicalInstrumentsApi/update")
        suspend fun updateMusicalInstrument(@Body musicalInstrument: MusicalInstrument): MusicalInstrument

        @DELETE("/musicalInstrumentsApi/delete/{id}")
        suspend fun deleteMusicalInstrumentById(@Path("id") id: Long)
    }

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build()


    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(
        MusicalInstrumentApi.Service::class.java)
}