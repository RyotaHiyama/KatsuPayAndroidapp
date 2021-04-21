package com.sample.katsupay.communication.data

import com.fasterxml.jackson.annotation.JsonProperty

data class Transaction(
    @JsonProperty("customerId")      val customerId:String,
    @JsonProperty("storeId")         val storeId:String,
    @JsonProperty("transactionTime") val time:String,
    @JsonProperty("price")           val price:String,
    @JsonProperty("productInfoList") val products:Array<String>?,
    @JsonProperty("charge")          val isCharge:String
) {
    override fun toString() : String {
        var prods = ""
        return if(products != null) {
            products.forEach {
                prods += "$it, "
            }
            prods = prods.substring(0, prods.length - 2)
            "顧客ID：$customerId\n購入店舗ID：$storeId\n時間：$time\n取引金額：$price\n購入商品：$prods\n"
        } else "顧客ID：$customerId\n購入店舗ID：$storeId\n時間：$time\nチャージ金額：$price\n"
    }
}