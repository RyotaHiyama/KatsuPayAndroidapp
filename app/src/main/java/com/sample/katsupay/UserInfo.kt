package com.sample.katsupay

import java.util.Date

open class UserInfo {
    companion object {
        lateinit var customer_id: String // サーバアクセス用のID
        lateinit var email: String // Eメールアドレス
        lateinit var purchaseHistory: String // 購入履歴
        lateinit var birthday: String // 誕生日
        private lateinit var balance: String // 残高
        private lateinit var password: Password // パスワード

        fun initPassword(pass:String) : Boolean {
            if(::password.isInitialized) {
                return false
            } else {
                password = Password(pass)
                return true
            }
        }

        fun getBalance() : String { return balance }
        fun setBalance(balance:String) { this.balance = balance }
    }

    private fun birthDayStringToDate(birthDayString:String) :Date {
        return Date()
    }
}