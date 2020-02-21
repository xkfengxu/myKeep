package com.fengxu.mykeep.widget

import android.os.CountDownTimer

/**
 * @author fengxu
 * @createDate 2020 02 21
 * @description 倒计时工具
 */
class TimeCount(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(
    millisInFuture,
    countDownInterval
) {
    private var mCountOver: CountOver? = null

    override fun onTick(millisUntilFinished: Long) {
        if (mCountOver != null) {
            mCountOver!!.onCountTick(millisUntilFinished)
        }
    }

    override fun onFinish() {
        cancel()
        if (mCountOver != null) {
            mCountOver!!.onCountOver(true)
        }
    }

    fun setOnCountOverListener(countOver: CountOver?) {
        mCountOver = countOver
    }

    /**
     * 倒计时回调接口
     */
    interface CountOver {
        fun onCountOver(isFinish: Boolean)
        fun onCountTick(millisUntilFinished: Long)
    }

    open class SimpleCountOverListener : CountOver {
        override fun onCountOver(isFinish: Boolean) {}
        override fun onCountTick(millisUntilFinished: Long) {}
    }
}