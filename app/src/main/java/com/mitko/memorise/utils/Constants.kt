package com.mitko.memorise.utils

import android.view.animation.PathInterpolator
import androidx.compose.animation.core.Easing

object Constants {
    const val DURATION = 600
    const val DURATION_ENTER = 400
    const val DURATION_ENTER_SHORT = 300
    const val DURATION_ENTER_LONG = 500
    const val DURATION_EXIT = 200
    const val DURATION_EXIT_SHORT = 100
    const val DURATION_EXIT_LONG = 300

    private val emphasizedPath = android.graphics.Path().apply {
        moveTo(0f, 0f)
        cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
        cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
    }

    val emphasizedDecelerate = PathInterpolator(0.05f, 0.7f, 0.1f, 1f)
    val emphasizedAccelerate = PathInterpolator(0.3f, 0f, 0.8f, 0.15f)
    val emphasized = PathInterpolator(emphasizedPath)

    val EmphasizedEasing: Easing = Easing { fraction -> emphasized.getInterpolation(fraction) }
    val EmphasizedDecelerateEasing =
        Easing { fraction -> emphasizedDecelerate.getInterpolation(fraction) }
    val EmphasizedAccelerateEasing =
        Easing { fraction -> emphasizedAccelerate.getInterpolation(fraction) }
}