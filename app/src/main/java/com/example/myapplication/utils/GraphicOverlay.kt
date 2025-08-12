package com.example.myapplication.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    
    private var imageWidth: Int = 0
    private var imageHeight: Int = 0
    private var isImageFlipped: Boolean = false

    fun setImageSourceInfo(imageWidth: Int, imageHeight: Int, isImageFlipped: Boolean) {
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        this.isImageFlipped = isImageFlipped
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 여기에 얼굴 박스나 다른 그래픽 요소를 그릴 수 있습니다
    }
}