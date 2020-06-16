package com.cimcitech.retrofotrequesttest.network

import android.util.Log
import androidx.core.text.htmlEncode
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

object ServiceCreator {

    private const val BASE_URL = "http://106.124.129.134:8080/SafeDrivingWorld/"

    private const val BASE_URL_CAIYUN = "https://api.caiyunapp.com/"

    private const val BASE_URL_UPLOAD = "http://10.43.10.68:8888/"

    private val httpLogging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
        override fun log(message: String) {
            Log.e("MainActivity", message)
        }
    })

    private val httpLoggingInterceptor = httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()


    private val resultUpload = Retrofit.Builder()
        .baseUrl(BASE_URL_UPLOAD)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> create_upload(serviceClass: Class<T>): T = resultUpload.create(serviceClass)
    inline fun <reified T> create_upload(): T = create_upload(T::class.java)


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)

    private val retrofit_caiyun = Retrofit.Builder()
        .baseUrl(BASE_URL_CAIYUN)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> create_caiyun(serviceClass: Class<T>): T = retrofit_caiyun.create(serviceClass)
    inline fun <reified T> create_caiyun(): T = create_caiyun(T::class.java)
}