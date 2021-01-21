package com.sample.katsupay.signin_up

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentResult
import com.sample.katsupay.*
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.datas.DataChecker
import com.sample.katsupay.datas.datas.QRCodeData
import com.sample.katsupay.datas.datas.UserInfo
import kotlinx.android.synthetic.main.sign_up.*
import java.net.HttpURLConnection
import java.util.*

class SignUp : AppCompatActivity() {
    companion object {
        const val OK = 0
        const val ERROR_MAIL_ADDRESS = -1
        const val ERROR_BIRTHDAY     = -2
        const val ERROR_PASSWORD     = -3

        val calendar: Calendar = Calendar.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        val studentId = QRCodeData.stuId
        checkId(studentId)

//        if(isRegisteredId()) {
//            AlertDialog.Builder(this)
//                .setTitle("エラー")
//                .setMessage("登録済みのIDです")
//                .setPositiveButton("戻る"){ _, _ -> }
//                .show()
//            UserInfo.delete()
//            QRCodeData.stuId = null
//            intent = Intent(this, ScanQRCode::class.java)
//            startActivity(intent)
//        }

        /* 戻るボタン */
        val signIn = Intent(this, SignIn::class.java)
        signUpReturn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("最終確認")
                .setMessage("アカウント登録を中断しますか？")
                .setPositiveButton("はい"){ _, _ ->
                    QRCodeData.stuId = null
                    startActivity(signIn)
                }
                .setNegativeButton("いいえ"){ _, _ -> }
                .show()
        }

        /* 登録完了ボタン */
        SignUpDecide.setOnClickListener {
            QRCodeData.stuId = null

            if(checkData() == OK) {
                AlertDialog.Builder(this)
                    .setTitle("最終確認")
                    .setMessage("アカウント登録を完了しますか？")
                    .setPositiveButton("確定"){ _, _ ->
                        setUserInfo()
                        signUp()
                        startActivity(signIn)
                    }
                    .setNegativeButton("戻る"){ _, _ -> }
                    .show()
            }
        }

        /* 生年月日 */
        birthDayPicker.gravity = Gravity.CENTER
        birthDayPicker.addView(setBirthDayPicker())
    }

    private fun isRegisteredId() : Boolean {
        val commServer = CommServer()
        commServer.setUrl(CommServer.LOGIN)
        commServer.execute(CommServer.UB)
        while(commServer.RESPONSE_CODE == -1) { /* wait for response */ }
        return commServer.RESPONSE_CODE == 404
    }

    private fun signUp() {
        val commServer = CommServer()
        commServer.setUrl(CommServer.SIGN_UP)
        commServer.execute(CommServer.UB)

        while(commServer.RESPONSE_CODE == -1) {
            /* wait for response */
        }

        if(commServer.RESPONSE_CODE == HttpURLConnection.HTTP_OK) {
            Toast.makeText(this, "登録完了", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "登録できませんでした", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUserInfo() {
        val pYear = findViewById<NumberPicker>(R.id.pYear)
        val pMonth =  findViewById<NumberPicker>(R.id.pMonth)
        val pDays =  findViewById<NumberPicker>(R.id.pDay)
        val year = pYear.displayedValues[pYear.value].toInt()
        val month = pMonth.displayedValues[pMonth.value].toInt()
        val day = pDays.displayedValues[pDays.value].toInt()

        UserInfo.birthday = "$year/$month/$day"
        UserInfo.email = emailAddress.text.toString()
        UserInfo.initPassword(signUpPasswordEdit.text.toString())
    }

    private fun checkId(studentId:IntentResult?) {
        if(studentId == null) {
            QRCodeData.stuId = null
            intent = Intent(this, ScanQRCode::class.java)
            startActivity(intent)
        } else {
            if(studentId.contents == null){
                QRCodeData.stuId = null
                Toast.makeText(this, "正常に読み込めませんでした", Toast.LENGTH_LONG).show()
                intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            } else {
                val decodeStuId = decode(studentId.contents)
                if (DataChecker.isUserId(decodeStuId, this)) setId(studentId)
            }
        }
    }

    override fun onBackPressed() {
        QRCodeData.stuId = null
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)
    }

    private fun checkData() : Int {
        if(!DataChecker.isMailAddress(emailAddress.text.toString(), this)) {
            return ERROR_MAIL_ADDRESS
        }

        if(!checkBirthDay()) {
            return ERROR_BIRTHDAY
        }

        if(!password()) {
            return ERROR_PASSWORD
        }

        return OK
    }

    private fun password() : Boolean {
        val p1 = signUpPasswordEdit.text.toString()
        val p2 = signUpPasswordVerificationEdit.text.toString()

        /* check that password equal verification */
        if(p1 != p2) {
            AlertDialog.Builder(this)
                .setTitle("パスワードの不一致")
                .setMessage("パスワードを確認してください")
                .setPositiveButton("OK"){ _, _ -> }
                .show()
            return false
        }

        DataChecker.isPassword(p1, this)

        return true
    }

    private fun checkBirthDay() : Boolean {
        val pYear = findViewById<NumberPicker>(R.id.pYear)
        val pMonth =  findViewById<NumberPicker>(R.id.pMonth)
        val pDays =  findViewById<NumberPicker>(R.id.pDay)

        return DataChecker.isBirthDay(pYear, pMonth, pDays, this)
    }

    private fun setId(stuId:IntentResult) {
        val stuId = decode(stuId.contents)
        signUpId.setText(stuId)
        signUpId.isEnabled = false
        signUpId.setTextColor(Color.BLACK)
        UserInfo.customer_id = stuId
    }

    private fun decode(code:String) : String {
        val key = 0b101101000101011011000101
        val codeNum = Integer.parseInt(code)
        Log.i("*******GET DATA", code + " -> " + (key xor codeNum).toString())
        return (key xor codeNum).toString()
    }

    private fun setBirthDayPicker() : LinearLayout {
        val layout = LinearLayout(this)
        layout.gravity = Gravity.CENTER

        /* year */
        layout.addView(yearPicker())
        val tYear  = TextView(this)
        tYear.text = "年"
        layout.addView(tYear)

        /* month */
        layout.addView(monthPicker())
        val tMonth   = TextView(this)
        tMonth.text = "月"
        layout.addView(tMonth)

        /* day */
        layout.addView(dayPicker())
        val tDay  = TextView(this)
        tDay.text = "日"
        layout.addView(tDay)

        return layout
    }

    private fun dayPicker() : NumberPicker {
        val degreesValues = arrayOfNulls<String>(31)
        for (i in 0 until 31) {
            degreesValues[i] = (i + 1).toString()
        }

        val picker = NumberPicker(this)
        picker.minValue = 0
        picker.maxValue = 30
        picker.displayedValues = degreesValues
        picker.id = R.id.pDay
        return picker
    }

    private fun monthPicker() : NumberPicker {
        val degreesValues = arrayOfNulls<String>(12)
        for (i in 0 until 12) {
            degreesValues[i] = (i + 1).toString()
        }

        val picker = NumberPicker(this)
        picker.minValue = 0
        picker.maxValue = 11
        picker.displayedValues = degreesValues
        picker.id = R.id.pMonth
        return picker
    }

    private fun yearPicker() : NumberPicker {
        val degreesValues = arrayOfNulls<String>(100)
        val startYear = calendar.get(Calendar.YEAR) - 100
        for (i in 0 until 100) {
            degreesValues[i] = (startYear + i).toString()
        }

        val picker = NumberPicker(this)
        picker.minValue = 0
        picker.maxValue = 99
        picker.value = 70
        picker.displayedValues = degreesValues
        picker.id  = R.id.pYear
        return picker
    }
}