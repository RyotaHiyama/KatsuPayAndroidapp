package com.sample.katsupay.signin_up

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.sample.katsupay.R
import com.sample.katsupay.data.data.QRCodeData

class ScanQRCode : AppCompatActivity() {
    companion object {
        const val SIGN_UP = "SIGN_UP"
    }
    private var layout = SignUp::class.java

    @Deprecated("This method don't needed if QR scanning be used only Sign-up")
    fun setReturnLayout(ret:String) {
        when(ret) {
            SIGN_UP -> {
                layout = SignUp::class.java
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_scanner)

        IntentIntegrator(this)
            .setOrientationLocked(true)
            .setBeepEnabled(false)
            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .initiateScan()
    }

    override fun onResume() {
        super.onResume()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val stuId = result.contents
            if(stuId == null) {
                QRCodeData.stuId = null
                Toast.makeText(this, "正常に読み込めませんでした", Toast.LENGTH_LONG).show()
                intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            } else {
                QRCodeData.stuId = result
                Toast.makeText(this, "学籍番号の読み込みに成功しました", Toast.LENGTH_LONG).show()

                val intent = Intent(this, SignUp::class.java)
                startActivity(intent)
            }
        } else {
            QRCodeData.stuId = null
            Toast.makeText(this, "正常に読み込めませんでした", Toast.LENGTH_LONG).show()
            intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }
    }
}