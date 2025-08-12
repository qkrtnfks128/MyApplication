package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.face.Face
import com.example.myapplication.viewmodel.login.DetectingViewModel

class FaceDetectorProcessor(
    context: Context,
    detectorOptions: com.google.mlkit.vision.face.FaceDetectorOptions?,
    viewModel: DetectingViewModel
) : VisionImageProcessor<Face>(context) {

    private var croppedImage: Bitmap? = null
    private var imageBitmap: Bitmap? = null
    private var viewModel = viewModel

    fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap? {
        val w: Int = rect.right - rect.left
        val h: Int = rect.bottom - rect.top

        // Bitmap.Config가 nullable이므로 안전하게 처리
        val config = bitmap.config ?: Bitmap.Config.ARGB_8888

        val ret = Bitmap.createBitmap(w, h, config)
        val canvas = Canvas(ret)
        canvas.drawBitmap(bitmap, -rect.left.toFloat(), -rect.top.toFloat(), null)
        return ret
    }

    override fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay) {
        for (face in faces) {
            if (imageBitmap != null) {
                croppedImage = cropBitmap(imageBitmap!!, face.boundingBox)
            }
        }
        // 얼굴 사진 캡처 후 ViewModel로 전달
        croppedImage?.let { viewModel.checkFace(it) }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Face detection failed $e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }
}