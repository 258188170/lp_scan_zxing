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
            intent.putExtra(Intents.Scan.FORMATS, Intents.Scan.QR_CODE_MODE)
            intent.putExtra(Intents.Scan.ACTION, Intents.Scan.ACTION)
            startActivityForResult(intent, 10086)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val stringExtra = data?.getStringExtra(Intents.Scan.RESULT)
            val rowByte = data?.getByteArrayExtra(Intents.Scan.RESULT_BYTES)
            if (rowByte != null && rowByte.isNotEmpty()) {
                lpDecode(rowByte)
            }

        }
    }

    private fun lpDecode(rowByte: ByteArray) {
        val splitLevelString = CodeDecJni.splitLevelString(rowByte)
        val readableCode = splitLevelString[0][1]
        val image = splitLevelString[1][1]
        if (image.isNotEmpty()){
            CodeDecJni.ImageDecode(image)
            Log.d(TAG, "机读码: ${ConvertUtils.bytes2String(readableCode)}")
            Log.d(TAG, "图片: ${ConvertUtils.bytes2String(image)}")
        }

    }
}