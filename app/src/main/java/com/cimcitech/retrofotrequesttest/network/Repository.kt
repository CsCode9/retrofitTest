package com.cimcitech.retrofotrequesttest.network

import android.util.Log
import androidx.lifecycle.liveData
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.bean.responseBean.CustomerResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun getCustomer(customerRequest: CustomerRequest) = fire(Dispatchers.IO){
           val response =  Network.getCustomer(customerRequest)
            if (response.status.errorCode == "00"){
                Result.success(response.rows)
            }else{
                Result.failure(RuntimeException("数据有误"))
            }
    }


    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}

/*
enqueue(object :
                Callback<List<CustomerResponse.Row>> {
                override fun onFailure(call: Call<List<CustomerResponse.Row>>, t: Throwable) {
                    Result.failure<List<CustomerResponse.Row>>(t)
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
                        Result.success(customers)
                        for (customer in customers){
                            Log.e("请求到的数据", customer.toString())
                        }
                    }
                }
            })
* */