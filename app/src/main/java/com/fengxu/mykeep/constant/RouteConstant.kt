package com.fengxu.mykeep.constant

/**
 * @author fengxu
 */
class RouteConstant {
    /**
     * 路径标识管理
     * Group的值默认就是第一个 /  /（两个分隔符） 之间的内容
     */
    companion object {
        const val MY_KEEP = "/myKeep"
        const val ACTIVITY_MAIN = "$MY_KEEP/MainActivity"
        const val ACTIVITY_SPLASH = "$MY_KEEP/SplashActivity"
    }
}