package com.example.myapplication.utils

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.example.myapplication.utils.FrameMetadata
import java.nio.ByteBuffer

interface ImageProcessor {
    fun processByteBuffer(
        data: ByteBuffer,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay
    )

    fun processBitmap(bitmap: Bitmap, graphicOverlay: GraphicOverlay)

    fun processImageProxy(imageProxy: ImageProxy, graphicOverlay: GraphicOverlay)

    fun stop()
}