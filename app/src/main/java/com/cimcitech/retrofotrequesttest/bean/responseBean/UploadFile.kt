package com.cimcitech.retrofotrequesttest.bean.responseBean

/**
 *@Date 2020/6/16
 *@author Chen
 *@Description
 */
data class UploadFile(
    val code: Int,
    val fileNames: List<String>,
    val msg: String,
    val urls: List<String>
)