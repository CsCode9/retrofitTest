package com.cimcitech.retrofotrequesttest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cimcitech.retrofotrequesttest.network.Repository

/**
 *@Date 2020/5/9
 *@author Chen
 *@Description
 */
class PlaceViewModel: ViewModel() {

    private val placeLiveData = MutableLiveData<String>()

    val plaveData = Transformations.switchMap(placeLiveData){
        Repository.getPlace(it)
    }

    fun getPlace(query: String){
        placeLiveData.value = query
    }
}