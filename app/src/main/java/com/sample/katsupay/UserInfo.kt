package com.sample.katsupay

import java.util.Date

open class UserInfo {
    private var id:String // サーバアクセス用のID
    private var userName:String // ユーザネーム
    private var eMailAdd:String // Eメールアドレス
    private var birthDay:Date // 生年月日
    private var password:Password // パスワード
    private var balance:String // 残高
    private var purchaseHistory:String // 購入履歴

    /* 登録情報の初期化 */
    constructor(id:String, userName:String, eMailAdd:String, birthDayString:String,
                password:String, balance:String, purchaseHistory: String) {
        this.id = id;this.userName = userName;this.eMailAdd = eMailAdd
        this.balance = balance;this.purchaseHistory = purchaseHistory
        this.birthDay = birthDayStringToDate(birthDayString)
        this.password = Password(password)
    }

    private fun birthDayStringToDate(birthDayString:String) :Date {
        return Date()
    }
}