package com.fengxu.mykeep.widget.banner

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import java.util.*

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 指示器基类
 */
abstract class BaseIndicator(context: Context?) :
    LinearLayout(context), Indicator {
    private var mCellCount = 0
    private var mCurrentPos = 0
    private var mCellViews: MutableList<IndicatorCell>? = null
    private fun init() {
        mCellViews = ArrayList()
    }

    override fun setCellCount(cellCount: Int) {
        mCellCount = cellCount
        var i = 1
        while (i <= mCellCount) {
            val view: IndicatorCell = getCellView()
            mCellViews!!.add(view)
            addView(view)
            i++
        }
    }

    override fun setCurrentPosition(currentPosition: Int) {
        mCurrentPos = currentPosition
        invalidateCell()
    }

    protected abstract fun getCellView(): IndicatorCell
    /**
     * 指示器小圆点半径
     */
    protected abstract fun getCellWidth(): Float

    /**
     * 指示器小圆点间距
     */
    protected abstract fun getCellMargin(): Float

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 重新测量当前界面的宽度
        var width =
            paddingLeft + paddingRight + getCellMargin() * mCellCount + getCellMargin() * (mCellCount - 1)
        var height = paddingTop + paddingBottom + getCellWidth()
        width = View.resolveSize(width.toInt(), widthMeasureSpec).toFloat()
        height = View.resolveSize(height.toInt(), heightMeasureSpec).toFloat()
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        invalidateCell()
    }

    private fun invalidateCell() {
        for (i in 0 until mCellCount) {
            val view: IndicatorCell = mCellViews!![i]
            val left = i * getCellWidth() + getCellMargin() * i
            //float right = (i + 1) * getCellWidth() + getCellMargin() * i;
            view.left = left.toInt()
            view.top = height / 2 - getCellWidth().toInt() / 2
            view.layoutParams.width = getCellWidth().toInt()
            view.layoutParams.height = getCellWidth().toInt()
            if (i == mCurrentPos) {
                view.select()
            } else {
                view.unSelect()
            }
            view.invalidate()
        }
        invalidate()
    }

    /**
     * dp转px
     *
     * @param dpVal dp value
     * @return px value
     */
    protected fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal,
            context.resources.displayMetrics
        ).toInt()
    }

    init {
        init()
    }
}