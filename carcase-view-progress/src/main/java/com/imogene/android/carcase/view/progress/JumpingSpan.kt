package com.imogene.android.carcase.view.progress

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.style.ReplacementSpan

internal class JumpingSpan : ReplacementSpan() {

    var translationX = 0f

    var translationY = 0f

    private var initialAlpha = -1

    var alpha = 1F

    override fun getSize(paint: Paint, text: CharSequence,
                         start: Int, end: Int,
                         fontMetricsInt: FontMetricsInt?): Int {
        return paint.measureText(text, start, end).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence,
                      start: Int, end: Int, x: Float,
                      top: Int, y: Int, bottom: Int, paint: Paint) {
        if(initialAlpha == -1){
            initialAlpha = paint.alpha
        }
        paint.alpha = (alpha * initialAlpha).toInt()
        canvas.drawText(text, start, end, x + translationX, y + translationY, paint)
    }
}
