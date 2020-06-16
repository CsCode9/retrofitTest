package com.cimcitech.retrofotrequesttest.network

import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.bean.responseBean.UploadFile
import okhttp3.MultipartBody
import retrofit2.await

object Network {

    private val uploadFileService = ServiceCreator.create_upload<FileUpload>()

    suspend fun uploadFile(partList: List<MultipartBody.Part>) = uploadFileService.uploadFile(partList).await()

    private val appService = ServiceCreator.create<CustomerService>()

    suspend fun getCustomer(customerRequest: CustomerRequest) = appService.getCustomer(customerRequest).await()

    private val caiyunService = ServiceCreator.create_caiyun<PlaceService>()

    suspend fun getPlace(query: String) = caiyunService.getPlace(query).await()
}