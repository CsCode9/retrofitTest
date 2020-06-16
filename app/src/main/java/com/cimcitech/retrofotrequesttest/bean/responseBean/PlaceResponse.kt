package com.cimcitech.retrofotrequesttest.bean.responseBean

/**
 *@Date 2020/5/9
 *@author Chen
 *@Description
 */
data class PlaceResponse(
    val places: List<Place>,
    val query: String,
    val status: String
){
    data class Place(
        val formatted_address: String,
        val id: String,
        val location: Location,
        val name: String,
        val place_id: String
    )

    data class Location(
        val lat: Double,
        val lng: Double
    )
}

