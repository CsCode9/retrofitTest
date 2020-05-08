package com.cimcitech.retrofotrequesttest.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private val BASE_URL = "http://106.124.129.134:8080/SafeDrivingWorld/"

    private val httpLogging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
        override fun log(message: String) {
            Log.e("MainActivity", message)
        }
    })

    private val httpLoggingInterceptor = httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}