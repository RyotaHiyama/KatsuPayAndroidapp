package com.sample.katsupay.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sample.katsupay.communication.data.Customer
import com.sample.katsupay.communication.data.Product
import com.sample.katsupay.communication.data.Store
import com.sample.katsupay.communication.data.Transaction

object JsonParser {
    fun transactionParse(str:String) : List<Transaction>? {
        return if(str == "") null // 何も文字列が含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val jObjList:List<Transaction> = mapper.readValue(str)
            jObjList
        }
    }

    fun customerParse(str:String) : Customer? {
        return if(str == "") null
        else {
            val mapper = jacksonObjectMapper()
            val customer: Customer = mapper.readValue(str)
            customer
        }
    }

    fun stockParse(str: String) : List<Product>? {
        return if (str == "") null
        else {
            val mapper = jacksonObjectMapper()
            val products:List<Product> = mapper.readValue(str)
            products
        }
    }

    fun storesParse(str: String) : List<Store>? {
        return if(str == "") null
        else {
            val mapper = jacksonObjectMapper()
            val stores:List<Store> = mapper.readValue(str)
            stores
        }
    }
}