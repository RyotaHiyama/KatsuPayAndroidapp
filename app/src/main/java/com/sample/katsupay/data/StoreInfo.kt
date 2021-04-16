package com.sample.katsupay.data

import com.sample.katsupay.activity.Password

open class StoreInfo {
    companion object {
        var storeId: String? = null
        private var password: Password? = null
        var email: String? = null
        var birthday: String? = null
    }
}