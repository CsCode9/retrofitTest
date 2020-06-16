package com.cimcitech.retrofotrequesttest.network

import android.util.Log
import androidx.lifecycle.liveData
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.CoroutineContext


object Repository {

    fun uploadFile(list: List<File>) = fire(Dispatchers.IO){
        //创建表单map,里面存储服务器本接口所需要的数据;
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM) //在这里添加服务器除了文件之外的其他参数
        for (file in list) {
            val body = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            //添加文件(uploadfile就是你服务器中需要的文件参数)
            Log.e("--------文件-------", "uploadFile: ${file.isFile}" )
            Log.e("--------文件-------", "uploadFile: ${file.absolutePath}" )
            builder.addFormDataPart("files", file.name, body)
        }
        val parts = builder.build().parts
        val response = Network.uploadFile(parts)
        if (response.code == 200){
            Result.success(response.urls)
        }else{
            Result.failure(RuntimeException("上传失败"))
        }
    }

    fun getCustomer(customerRequest: CustomerRequest) = fire(Dispatchers.IO){
           val response =  Network.getCustomer(customerRequest)
            if (response.status.errorCode == "00"){
                Result.success(response.rows)
            }else{
                Result.failure(RuntimeException("数据有误"))
            }
    }

    fun getPlace(query: String) = fire(Dispatchers.IO){
        val response = Network.getPlace(query)
        if (response.status == "ok"){
            Result.success(response.places)
        }else{
            Result.failure(RuntimeException("response status is ${response.status}"))
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