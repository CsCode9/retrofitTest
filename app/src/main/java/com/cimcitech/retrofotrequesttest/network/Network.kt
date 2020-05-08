package com.cimcitech.retrofotrequesttest.network

import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import retrofit2.await

object Network {

    private val appService = ServiceCreator.create<CustomerService>()

    suspend fun getCustomer(customerRequest: CustomerRequest) = appService.getCustomer(customerRequest).await()
}