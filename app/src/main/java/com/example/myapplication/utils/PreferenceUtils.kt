package com.example.myapplication.utils

import android.content.Context
import com.google.mlkit.vision.face.FaceDetectorOptions

object PreferenceUtils {
    fun getFaceDetectorOptionsForLivePreview(context: Context): FaceDetectorOptions {
        return FaceDetectorOptions.Builder()
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()
    }

    fun isCameraLiveViewportEnabled(context: Context): Boolean {
        return true
    }

    fun getCameraXTargetAnalysisSize(context: Context): android.util.Size? {
        return null
    }

    fun getCameraXTargetResolution(context: Context): android.util.Size? {
        return null
    }
}