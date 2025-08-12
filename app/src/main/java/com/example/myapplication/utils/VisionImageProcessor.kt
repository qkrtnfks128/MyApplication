package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.*

abstract class VisionImageProcessor<T> {
    protected val detector: FaceDetector
    protected val context: Context

    constructor(context: Context) {
        this.context = context
        val options = FaceDetectorOptions.Builder()
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()
        detector = FaceDetection.getClient(options)
    }

    fun processImageProxy(imageProxy: ImageProxy, graphicOverlay: GraphicOverlay) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = com.google.mlkit.vision.common.InputImage.fromMediaImage(
                mediaImage, 
                imageProxy.imageInfo.rotationDegrees
            )
            detector.process(image)
                .addOnSuccessListener { faces ->
                    onSuccess(faces, graphicOverlay)
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    fun stop() {
        detector.close()
    }

    protected abstract fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay)
    protected abstract fun onFailure(e: Exception)
}