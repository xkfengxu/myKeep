package com.fengxu.mykeep.widget.banner

import android.content.Context
import android.graphics.Paint
import android.view.View

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 指示器单元
 */
abstract class IndicatorCell(context: Context?) :
    View(context) {
    protected var mPaint: Paint = Paint()
    /**
     * 选中时的绘制，子类自定义实现
     */
    abstract fun select()

    /**
     * 未选中时的绘制，子类自定义实现
     */
    abstract fun unSelect()

    init {
        mPaint.isAntiAlias = true
    }
}
