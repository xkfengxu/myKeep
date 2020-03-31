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
        private const val MY_KEEP = "/myKeep"
        const val ACTIVITY_MAIN = "$MY_KEEP/MainActivity"
        const val ACTIVITY_SPLASH = "$MY_KEEP/SplashActivity"
        const val ACTIVITY_CAMERA = "$MY_KEEP/CameraActivity"
        const val ACTIVITY_VIDEO_PLAYER = "$MY_KEEP/VideoPlayerActivity"
    }
}