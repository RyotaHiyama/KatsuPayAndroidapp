package com.sample.katsupay.communication.data

import com.fasterxml.jackson.annotation.JsonProperty

data class Store (
    @JsonProperty("storeId")  var storeId:String,
    @JsonProperty("password")    var password:String?,
    @JsonProperty("email")       var email:String?,
    @JsonProperty("birthday")    var birthday:String?
) {

}