package com.fengxu.mykeep.widget.banner

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description
 */
class ScalePageTransformer : ViewPager.PageTransformer {

    companion object {
        private const val MIN_SCALE = 0.9f
    }

    private fun handleInvisiblePage(view: View) {
        view.scaleY = MIN_SCALE
    }

    private fun handleLeftPage(view: View, position: Float) {
        val scale = MIN_SCALE.coerceAtLeast(1 - abs(position))
        view.scaleY = scale
    }

    private fun handleRightPage(view: View, position: Float) {
        val scale = MIN_SCALE.coerceAtLeast(1 - abs(position))
        view.scaleY = scale
    }

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1.0f -> {
                handleInvisiblePage(page)
            }
            position <= 0.0f -> {
                handleLeftPage(page, position)
            }
            position <= 1.0f -> {
                handleRightPage(page, position)
            }
            else -> {
                handleInvisiblePage(page)
            }
        }
    }
}
