package com.fengxu.mykeep.widget.banner

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 指示器接口
 */
interface Indicator {
    fun setCellCount(cellCount: Int)
    fun setCurrentPosition(currentPosition: Int)
}