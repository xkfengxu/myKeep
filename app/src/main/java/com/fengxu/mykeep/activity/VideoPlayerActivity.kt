package com.fengxu.mykeep.activity

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.fengxu.mykeep.R
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.constant.RouteConstant
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

@Route(path = RouteConstant.ACTIVITY_VIDEO_PLAYER)
class VideoPlayerActivity : BaseActivity() {

    private var videoPlayer: StandardGSYVideoPlayer? = null

    private var orientationUtils: OrientationUtils? = null

    override fun intiView() {
        videoPlayer = findViewById<View>(R.id.video_player) as StandardGSYVideoPlayer

        val source1 =
            "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
        videoPlayer!!.setUp(source1, true, "测试视频")
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.mipmap.ic_launcher)
        videoPlayer!!.thumbImageView = imageView
        //增加title
        videoPlayer!!.titleTextView.visibility = View.VISIBLE
        //设置返回键
        videoPlayer!!.backButton.visibility = View.VISIBLE
        //设置旋转
        orientationUtils = OrientationUtils(this, videoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer!!.fullscreenButton
            .setOnClickListener { orientationUtils!!.resolveByClick() }
        //是否可以滑动调整
        videoPlayer!!.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer!!.backButton.setOnClickListener { onBackPressed() }
        videoPlayer!!.startPlayLogic()
        //设置状态栏颜色
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    override fun onPause() {
        super.onPause()
        videoPlayer!!.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer!!.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //先返回正常状态
        if (orientationUtils!!.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer!!.fullscreenButton.performClick()
            return
        }
        //释放所有
        videoPlayer!!.setVideoAllCallBack(null)
        super.onBackPressed()
    }

    override fun getContentView(): Int {
        return R.layout.activity_video_player
    }
}