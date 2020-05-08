package com.cimcitech.retrofotrequesttest.network

import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.bean.responseBean.CustomerResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerService {

    @POST("customer/getCustomerByPage")
    fun getCustomer(@Body customerRequest: CustomerRequest): Call<CustomerResponse>
}