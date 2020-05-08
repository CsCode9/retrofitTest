package com.cimcitech.retrofotrequesttest.bean.requestBean



/*{
    "firstIndex": 0,
    "pageSize": 10,
    "sort": 0,
    "intQuery": 4,
    "isEnable": 0,
    "orgs": [
        {
            "pkOrg": "1001A11000000088888"
        }
    ],
    "state": 2
}*/
data class CustomerRequest(
    val firstIndex: Int = 0,
    val intQuery: Int = 4,
    val isEnable: Int = 0,
    val orgs: List<Org> = listOf(Org()),
    val pageSize: Int = 1000,
    val sort: Int = 0,
    val state: Int = 2
)

data class Org(
    val pkOrg: String = "1001A11000000088888"
)