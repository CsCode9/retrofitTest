package com.cimcitech.retrofotrequesttest.network

import com.cimcitech.retrofotrequesttest.bean.responseBean.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 *@Date 2020/5/9
 *@author Chen
 *@Description
 */
interface PlaceService {

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("v2/place?token=E0QkjrXRsqxmhVBh/&lang=zh_CN")
    fun getPlace(@Query("query") query: String): Call<PlaceResponse>
}