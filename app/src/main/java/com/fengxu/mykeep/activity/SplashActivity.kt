package com.fengxu.mykeep.activity

import android.text.TextUtils
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.fengxu.mykeep.R
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.constant.RouteConstant
import com.fengxu.mykeep.http.RetrofitHelper
import com.fengxu.mykeep.http.api.RapApi
import com.fengxu.mykeep.utils.Key
import com.fengxu.mykeep.widget.TimeCount
import com.fengxu.mykeep.widget.TimeCount.SimpleCountOverListener
import com.tencent.mmkv.MMKV

@Route(path = RouteConstant.ACTIVITY_SPLASH)
class SplashActivity : BaseActivity() {

    /**
     * timerDuration ：页面持续时间
     * refreshTime ： 页面刷新时间
     */
    private val timerDuration = 3000L
    /**
     * lottie动画控件
     */
    private var lottie: LottieAnimationView? = null

    override fun intiView() {
        //开屏页动画
//        val openingStartAnimation = OpeningStartAnimation.Builder(this)
//            .setAppStatement(resources.getString(R.string.app_statement))
//            .setAnimationInterval(animationInterval)
//            .create()
//        openingStartAnimation.show(this)
        //refreshTime ： 页面刷新时间
        val refreshTime = 1000L
        //开屏页倒计时
        val timeCount = TimeCount(timerDuration, refreshTime)
        lottie = findViewById(R.id.animation_view)
        timeCount.setOnCountOverListener(object : SimpleCountOverListener() {
            override fun onCountOver(isFinish: Boolean) {
                gotoMain()
            }

            override fun onCountTick(millisUntilFinished: Long) {
                //越跑越快的效果
                lottie?.speed =
                    ((timerDuration - millisUntilFinished) / 500L).toFloat()
//                skip.text =
//                    resources.getString(R.string.skip, (millisUntilFinished / 1000L).toInt() + 1)
            }
        })
        timeCount.start()
        //初始化
        MMKV.initialize(this)
        //请求轮播图url
        getBannerUrl()
        //跳过按钮，目前隐藏
        val skip = findViewById<TextView>(R.id.tv_skip)
        skip.setOnClickListener { gotoMain() }
    }

    /**
     * 跳转首页
     */
    fun gotoMain() {
        ARouter.getInstance().build(RouteConstant.ACTIVITY_MAIN)
            .navigation()
        finish()
    }

    /**
     * 请求轮播图url
     */
    private fun getBannerUrl() {
        RetrofitHelper.instance.requestListData<String>({
            RetrofitHelper.instance.getRapApi(RapApi::class.java).getBanner()
        }, {
            it.data?.let { _ ->
                val jsonUrl = JSON.toJSONString(it.data)
                if (!TextUtils.isEmpty(jsonUrl)) {
                    MMKV.defaultMMKV().encode(Key.BANNER_URL, jsonUrl)
                }
            }
        }, {})
    }

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }
}