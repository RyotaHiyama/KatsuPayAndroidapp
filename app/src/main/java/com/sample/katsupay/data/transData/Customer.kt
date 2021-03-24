package com.sample.katsupay.data.transData

import com.fasterxml.jackson.annotation.JsonProperty
import com.sample.katsupay.data.data.UserInfo
import kotlin.system.exitProcess

data class Customer(
    @JsonProperty("customerId")  var customerId:String,
    @JsonProperty("password")    var password:String,
    @JsonProperty("email")       var email:String,
    @JsonProperty("birthday")    var birthday:String
) {
    companion object {
        fun getUserInfoAsCustomer() : Customer {
            val id = UserInfo.customer_id ?: exitProcess(-1)
            return Customer(
                id,
                UserInfo.getPassword(),
                UserInfo.email,
                UserInfo.birthday
            )
        }
    }
}