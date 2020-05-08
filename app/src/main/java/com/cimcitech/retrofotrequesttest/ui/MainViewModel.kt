package com.cimcitech.retrofotrequesttest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.network.Repository

class MainViewModel: ViewModel() {

    private val customerLiveData = MutableLiveData<CustomerRequest>()

    val customerData = Transformations.switchMap(customerLiveData){
        Repository.getCustomer(it)
    }

    fun getCustomer(customerRequest: CustomerRequest){
        customerLiveData.value = customerRequest
    }
}