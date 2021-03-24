package com.sample.katsupay.data.transData

import com.fasterxml.jackson.annotation.JsonProperty

data class Product(
    @JsonProperty("productId") var product_id:String,
    @JsonProperty("name")       var name:String,
    @JsonProperty("price")      var price:Int,
    @JsonProperty("onSale")    var on_sale:String,
    @JsonProperty("stock")      var stock:Int,
    @JsonProperty("productInfo")var product_info:String
) {

}