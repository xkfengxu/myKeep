package com.fengxu.mykeep.widget.banner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 小圆点指示器
 */
class CircleIndicator(context: Context?) : BaseIndicator(context) {
    override fun getCellMargin(): Float {
        return dp2px(8F).toFloat()
    }

    override fun getCellWidth(): Float {
        return dp2px(7F).toFloat()
    }

    override fun getCellView(): IndicatorCell {
        return CircleCell(context)
    }

    inner class CircleCell(context: Context?) : IndicatorCell(context) {
        override fun select() {
            mPaint.color = Color.parseColor("#ffffff")
        }

        override fun unSelect() {
            mPaint.color = Color.parseColor("#000000")
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawCircle(getCellWidth() / 2, getCellWidth() / 2, getCellWidth() / 2, mPaint)
        }
    }
}