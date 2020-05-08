package com.cimcitech.retrofotrequesttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.bean.responseBean.CustomerResponse
import com.cimcitech.retrofotrequesttest.network.CustomerService
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "http://106.124.129.134:8080/SafeDrivingWorld/"

    private val httpLogging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
        override fun log(message: String) {
            Log.e("MainActivity", message)
        }
    })

    private val httpLoggingInterceptor = httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startRequest.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val appService = retrofit.create(CustomerService::class.java)
            appService.getCustomer(customerRequest = CustomerRequest()).enqueue(object : Callback<List<CustomerResponse.Row>>{
                override fun onFailure(call: Call<List<CustomerResponse.Row>>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("--------------数据返回失败", "测试是否走到这里")
                }
                override fun onResponse(
                    call: Call<List<CustomerResponse.Row>>,
                    response: Response<List<CustomerResponse.Row>>
                ) {
                    Log.e("--------------数据返回成功", "测试是否走到这里")
                    val customers = response.body()
                    if (customers != null){
                        for (customer in customers){
                            Log.e("请求到的数据", customer.toString())
                        }
                    }
                }
            })
        }
    }
}
