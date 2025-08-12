package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.myapplication.R
import timber.log.Timber
import java.io.*

import kotlin.random.Random


/**
 * Created by Douner on 2019. 4. 12..
 */
object ConvertUtils {
    /***
     * BASE64 Encoding
     * */
    fun encoder(input: String): String {
//        val encodedString: String = Base64.getEncoder().encodeToString(input.toByteArray())
        val encodedString: String =
            Base64.encodeToString(input.toByteArray(), android.util.Base64.NO_WRAP)
        return encodedString
    }

    fun bitmapToString(result: Bitmap?): String {
        var baos = ByteArrayOutputStream()
        result!!.compress(Bitmap.CompressFormat.JPEG, 40, baos)
        var b = baos.toByteArray()
        var strBitmap = Base64.encodeToString(b, Base64.DEFAULT)
        return strBitmap
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        try {
            var encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            var bitmap: Bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            return bitmap
        } catch (e: Exception) {
            e.message
            return null
        }
    }

    fun saveBitmapToFileCache(
        context: Context,
        bitmap: Bitmap,
        cvid: String
    ): String {

        val storage = context.getCacheDir()
        val tempFile = File(storage, "$cvid.jpg")
        var out: OutputStream? = null

        try {
            tempFile.createNewFile()  // 파일을 생성해주고

            var out: FileOutputStream = FileOutputStream(tempFile)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out)
            out.close()// 마무리로 닫아줍니다.
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return tempFile.absolutePath
    }


    fun getRandomSticker(): Int {
        val index = Random.nextInt(bgSticker.size)
        Timber.d("getRandomVideo [$index] [${bgSticker[index]}]")
        return bgSticker[index]
    }

    private val bgSticker: List<Int> = listOf(
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_background,
        R.mipmap.ic_launcher
    )
}