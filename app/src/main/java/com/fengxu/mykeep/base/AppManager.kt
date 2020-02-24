package com.fengxu.mykeep.base

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import java.util.*
import kotlin.system.exitProcess

/**
 * @author fengxu
 * @createDate 2020 02 21
 * @description Activity管理器
 */
class AppManager private constructor() {

    companion object {
        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }

    private val activityStack = Stack<Activity>()

    /**
     * 添加Activity到栈
     */
    fun pushActivity(activity: Activity?) {
        activityStack.push(activity)
    }

    /**
     * 移除栈顶的activity
     */
    fun popActivity() {
        if (activityStack.size < 1) {
            return
        }
        activityStack.pop()
    }

    /**
     * 结束指定的Activity(重载)
     */
    @Deprecated("never use")
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     */
    @Deprecated("never use")
    fun finishOthersActivity(cls: Class<*>) {
        for (i in activityStack.indices) {
            if (activityStack[i].javaClass != cls) {
                activityStack[i].finish()
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private fun finishAllActivity() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    /**
     * 应用程序退出
     */
    fun appExit() {
        finishAllActivity()
        ARouter.getInstance().destroy()
        exitProcess(0)
    }
}