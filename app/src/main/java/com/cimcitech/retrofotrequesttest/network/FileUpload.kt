package com.cimcitech.retrofotrequesttest.network

import com.cimcitech.retrofotrequesttest.bean.responseBean.UploadFile
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 *@Date 2020/6/16
 *@author Chen
 *@Description
 */
interface FileUpload {

    /**
     * 上传
     * Multipart 这个注解代表多表单上传
     * @param partList 表单信息
     * @return .
     */
    @Multipart
    @POST("/common/upload")
    fun uploadFile(@Part partList: List<MultipartBody.Part>): Call<UploadFile>
}