package com.fengxu.mykeep

import android.content.Context
import android.content.pm.ApplicationInfo
import com.alibaba.android.arouter.launcher.ARouter
import com.fengxu.mykeep.base.BaseApp
import kotlin.properties.Delegates

/**
 * @author fengxu
 * companion object 修饰为伴生对象,伴生对象在类中只能存在一个，类似于java中的静态方法 Java 中使用类访问静态成员，静态方法。
 */
class MyApplication : BaseApp() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun initConfig() {
        if (applicationInfo != null &&
            applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        ) {
            ARouter.openLog() // Print log
            ARouter.openDebug() // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this)
        CONTEXT = applicationContext
        VERSION_NAME = BuildConfig.VERSION_NAME
        SIGN = BuildConfig.SIGN
    }

}