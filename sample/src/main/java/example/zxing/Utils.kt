package example.zxing

import android.util.Log
import com.blankj.utilcode.util.ConvertUtils
import com.lpcode.decoding.v0047.l1.CodeDecJni

object Utils {
    private const val TAG = "Utils"
     fun decode(stringExtra: String?, rowByte: ByteArray?, resultFormat: String?) {
        try {
            if (resultFormat == "UPC_EAN_EXTENSION") {
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