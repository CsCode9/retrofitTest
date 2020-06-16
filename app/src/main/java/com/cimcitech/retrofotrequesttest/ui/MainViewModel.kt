package com.cimcitech.retrofotrequesttest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.network.Repository
import java.io.File

class MainViewModel: ViewModel() {

    private val customerLiveData = MutableLiveData<CustomerRequest>()
    private val fileUploadLiveData = MutableLiveData<List<File>>()

    val fileUploadData = Transformations.switchMap(fileUploadLiveData){
        Repository.uploadFile(it)
    }

    fun fileUpload(files: List<File>){
        fileUploadLiveData.value = files
    }

    val customerData = Transformations.switchMap(customerLiveData){
        Repository.getCustomer(it)
    }

    fun getCustomer(customerRequest: CustomerRequest){
        customerLiveData.value = customerRequest
    }


}