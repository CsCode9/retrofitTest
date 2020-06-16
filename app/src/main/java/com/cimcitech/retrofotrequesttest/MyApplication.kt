package com.cimcitech.retrofotrequesttest

import android.app.Application
import android.content.Context

/**
 *@Date 2020/6/16
 *@author Chen
 *@Description
 */
class MyApplication: Application() {

    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}