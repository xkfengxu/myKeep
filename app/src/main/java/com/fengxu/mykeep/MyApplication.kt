package com.fengxu.mykeep

import android.content.Context
import com.fengxu.mykeep.base.BaseApp
import kotlin.properties.Delegates

/**
 * @author fengxu
 * companion object 修饰为伴生对象,伴生对象在类中只能存在一个，类似于java中的静态方法 Java 中使用类访问静态成员，静态方法。
 */
class MyApplication : BaseApp() {

    /**
     * 双重校验锁式单例
     */
    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun initConfig() {
        CONTEXT = applicationContext
        VERSION_NAME = BuildConfig.VERSION_NAME
        SIGN = BuildConfig.SIGN
    }

}