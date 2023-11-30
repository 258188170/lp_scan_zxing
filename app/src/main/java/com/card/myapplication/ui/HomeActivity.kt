package com.card.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.blankj.utilcode.util.ConvertUtils
import com.card.myapplication.R
import com.google.zxing.client.android.CaptureActivity
import com.google.zxing.client.android.Intents
import com.lpcode.decoding.v0047.l1.CodeDecJni

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bt = findViewById<Button>(R.id.bt_can)
        bt.setOnClickListener {
            val intent = Intent(this, CaptureActivity::class.java)
//            intent.putExtra(Intents.Scan.FORMATS, Intents.Scan.QR_CODE_MODE)
            intent.putExtra(Intents.Scan.ACTION, Intents.Scan.ACTION)
            intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE)
            intent.putExtra(Intents.Scan.WIDTH, 300)
            intent.putExtra(Intents.Scan.HEIGHT, 300)
            startActivityForResult(intent, 10086)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 10086) {
            val stringExtra = data?.getStringExtra(Intents.Scan.RESULT)
            val rowByte = data?.getByteArrayExtra(Intents.Scan.RESULT_BYTES)
            val resultFormat = data?.getStringExtra(Intents.Scan.RESULT_FORMAT)
            Log.d(TAG, "onActivityResult: ${rowByte?.size}")

            decode(stringExtra, rowByte, resultFormat)
        }
    }

    private fun decode(stringExtra: String?, rowByte: ByteArray?, resultFormat: String?) {
        try {
            if (resultFormat == "LON_BEI") {
                val splitLevelString = CodeDecJni.splitLevelString(rowByte)
                val readableCode = splitLevelString[0][1]//加密过得
                val image = splitLevelString[1][1]///加密过得
                if (image.isNotEmpty()) {
                    val imageDecode = CodeDecJni.ImageDecode(image)
                    Log.d(TAG, "机读码: ${ConvertUtils.bytes2String(readableCode)}")
                    Log.d(TAG, "图片: ${ConvertUtils.bytes2String(image)}")
//                    androidMessage.scanResult(
//                        ConvertUtils.bytes2String(readableCode),
//                        imageDecode,
//                        resultFormat
//                    ) {}
                }
            } else {
//                androidMessage.scanResult(stringExtra, null, resultFormat) {}
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}