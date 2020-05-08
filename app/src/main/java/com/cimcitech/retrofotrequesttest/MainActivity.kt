package com.cimcitech.retrofotrequesttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.bean.responseBean.CustomerResponse
import com.cimcitech.retrofotrequesttest.network.Network
import com.cimcitech.retrofotrequesttest.network.Repository
import com.cimcitech.retrofotrequesttest.ui.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startRequest.setOnClickListener {
            viewModel.getCustomer(CustomerRequest())
        }
        viewModel.customerData.observe(this, Observer {
            val customer = it.getOrNull()
            if (customer != null) Log.e("------------", "${customer.size}")
        })
    }
}
