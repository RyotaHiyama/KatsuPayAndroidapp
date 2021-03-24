package com.sample.katsupay.data.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.sample.katsupay.Password

open class StoreInfo {
    companion object {
        var storeId: String? = null
        private var password: Password? = null
        var email: String? = null
        var birthday: String? = null
    }
}