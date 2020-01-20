package com.fengxu.mykeep.base

import android.app.Application

/**
 * @author fengxu
 * Application 基类
 */
abstract class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initConfig()
    }

    companion object {
        var VERSION_NAME: String? = null
        var SIGN: String? = null
    }

    abstract fun initConfig()

}