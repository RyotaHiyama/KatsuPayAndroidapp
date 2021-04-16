package com.sample.katsupay.communication.data

import com.fasterxml.jackson.annotation.JsonProperty

data class Product(
    @JsonProperty("productId") var product_id:String,
    @JsonProperty("name")       var name:String,
    @JsonProperty("price")      var price:Int,
    @JsonProperty("onSale")    var on_sale:String,
    @JsonProperty("stock")      var stock:Int,
    @JsonProperty("productInfo")var product_info:String
) {
    fun getProductString() : String {
        return name
    }
}