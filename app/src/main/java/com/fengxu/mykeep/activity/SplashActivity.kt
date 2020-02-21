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
import site.gemus.openingstartanimation.OpeningStartAnimation

/**
 * @author fengxu
 */

@Route(path = RouteConstant.ACTIVITY_SPLASH)
class SplashActivity : BaseActivity() {

    private val timerDuration = 5000L
    private val refreshTime = 1000L
    private val animationInterval = 1500L

    override fun intiView() {
        //开屏页动画
        val openingStartAnimation = OpeningStartAnimation.Builder(this)
            .setAppStatement(resources.getString(R.string.app_statement))
            .setAnimationInterval(animationInterval)
            .create()
        openingStartAnimation.show(this)
        //初始化mmkv
        MMKV.initialize(this)
        getBannerUrl()
        //开屏页倒计时
        val timeCount = TimeCount(timerDuration, refreshTime)
        val skip = findViewById<TextView>(R.id.tv_skip)
        timeCount.setOnCountOverListener(object : SimpleCountOverListener() {
            override fun onCountOver(isFinish: Boolean) {
                gotoMain()
            }

            override fun onCountTick(millisUntilFinished: Long) {
                //越跑越快
                findViewById<LottieAnimationView>(R.id.animation_view).speed =
                    ((timerDuration - millisUntilFinished) / 500L).toFloat()
                skip.text =
                    resources.getString(R.string.skip, (millisUntilFinished / 1000L).toInt() + 1)
            }
        })
        timeCount.start()
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