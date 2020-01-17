package com.fengxu.mykeep.base

import android.app.Application
import android.content.Context

/**
 * @author fengxu
 */
abstract class BaseApp : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //方法过多报错时再玩
//        MultiDex.install(base)
    }

}