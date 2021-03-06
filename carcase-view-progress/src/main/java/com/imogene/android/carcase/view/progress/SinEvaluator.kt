package com.imogene.android.carcase.view.progress

import android.animation.TypeEvaluator
import kotlin.math.max
import kotlin.math.sin

internal class SinEvaluator : TypeEvaluator<Float> {

    override fun evaluate(fraction: Float, from: Float, to: Float) : Float {
        return max(0.0, sin(fraction * Math.PI * 2.0)).toFloat() * (to - from)
    }
}
