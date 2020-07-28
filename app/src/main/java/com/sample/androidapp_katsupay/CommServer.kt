package com.sample.androidapp_katsupay;

/*
サーバーとのやり取りを担うクラス．
 */
public class CommServer {
    //サーバーのIPアドレス
    private companion object val SERVER_IP_ADDRESS:String = ""

    //サーバーと通信可能かを確認する
    public fun checkCommStatus(): Boolean {
        return true //サーバーと通信可能：true 通信不可：false
    }

    //idとpasswordを鍵としてサーバーからユーザ情報を取得する
    public fun signIn(id:String, password:Password): UserInfo {
        return UserInfo() //サインイン成功：true サインイン失敗：false
    }

    //サーバーに対して最新のユーザ情報を送信する
    public fun signOut(userInfo:UserInfo): Boolean {
        return true //サインアウト成功：true サインアウト失敗：false
    }

    //ユーザ登録情報を変更する
    public fun userInfoModification(userInfo:UserInfo): Boolean {
        return true //登録内容の変更完了：true 予期せぬエラーで変更不可：false
    }

}