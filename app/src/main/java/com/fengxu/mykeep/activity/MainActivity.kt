package com.fengxu.mykeep.activity

import android.Manifest
import android.content.pm.PackageManager
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONArray
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.fengxu.mykeep.R
import com.fengxu.mykeep.adapter.CommonAdapter
import com.fengxu.mykeep.base.AppManager
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.bean.Action
import com.fengxu.mykeep.bean.Article
import com.fengxu.mykeep.constant.RouteConstant
import com.fengxu.mykeep.http.RetrofitHelper
import com.fengxu.mykeep.http.api.RapApi
import com.fengxu.mykeep.utils.Key
import com.fengxu.mykeep.widget.BannerView
import com.fengxu.mykeep.widget.banner.CircleIndicator
import com.scwang.smartrefresh.header.FunGameBattleCityHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.tencent.mmkv.MMKV
import tyrantgit.explosionfield.ExplosionField


@Route(path = RouteConstant.ACTIVITY_MAIN)
class MainActivity : BaseActivity() {

    /**
     * 动作数据列表
     */
    private val actionList = ArrayList<MultiItemEntity>()
    /**
     * 轮播图地址
     */
    private val bannerUrl: MutableList<String> = ArrayList()
    /**
     * 动作列表adapter
     */
    private var actionAdapter: CommonAdapter? = CommonAdapter(null)
    /**
     * 轮播图控件
     */
    private var mBannerView: BannerView? = null
    /**
     * lottie动画控件
     */
    private var lottie: LottieAnimationView? = null
    /**
     * 上次点击时间
     */
    private var lastClickTime: Long = 0

    override fun intiView() {
        //初始化全局控件
        mBannerView = findViewById(R.id.banner_view)
        lottie = findViewById(R.id.animation_view)
        //粒子爆炸控件
        val explosionField = ExplosionField.attach2Window(this)
        //测试，展示应用第三方框架
        val confirm = findViewById<TextView>(R.id.tv_confirm)
        confirm.setOnClickListener {
            showFloatView(this.localClassName)
        }
        lottie?.setOnClickListener {
            //粒子爆炸效果，并移除lottie控件
            explosionField.explode(lottie)
            lottie = null
        }
        //recyclerView 设置layoutManager，actionAdapter
        val recyclerView = findViewById<RecyclerView>(R.id.rl_video)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = actionAdapter
        //设置轮播图
        setBannerUrl()
        //设置并测试部分第三方框架
        setLottieAnimationView()
        testSmartRefreshLayout()
        testRecyclerViewAdapterHelper()
        //跳转扫码界面
        findViewById<ImageView>(R.id.scan_code).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // android 6.0以上需要动态申请权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1000)
                return@setOnClickListener
            }
            ARouter.getInstance().build(RouteConstant.ACTIVITY_CAMERA)
                .navigation()
        }
    }

    /**
     * 权限请求回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ARouter.getInstance().build(RouteConstant.ACTIVITY_CAMERA)
                .navigation()
        }
    }

    /**
     * 设置轮播图
     */
    @Suppress("UNCHECKED_CAST")
    private fun setBannerUrl() {
        //获取数据
        val cacheUrlString = MMKV.defaultMMKV().decodeString(Key.BANNER_URL)
        if (!TextUtils.isEmpty(cacheUrlString)) {
            bannerUrl.addAll(JSONArray.parseArray(cacheUrlString) as List<String>)
            mBannerView!!.setIndicator(CircleIndicator(applicationContext))
            mBannerView?.setBannerData(bannerUrl)
        }
    }

    /**
     * 设置Lottie动画
     */
    private fun setLottieAnimationView() {
        //speed值为负数时倒放
//        lottie?.speed = (-1).toFloat()
        //当你设置-1的时候就代表无限循环
        lottie?.repeatCount = -1
    }

    /**
     * 试用 SmartRefreshLayout
     * 设置刷新，加载时长
     * Header，Footer 可在代码或xml中配置，代码中优先级最高
     */
    private fun testSmartRefreshLayout() {
        val refreshLayout = findViewById<SmartRefreshLayout>(R.id.rf_video) as RefreshLayout
        //刷新事件监听，延时至少2秒，加载数据
        refreshLayout.setOnRefreshListener {
            it.finishRefresh(2000)
            getActionData()
        }
        refreshLayout.setOnLoadMoreListener { it.finishLoadMore(2000) }
        //可设置不同的刷新头部，底部
        refreshLayout.setRefreshHeader(FunGameBattleCityHeader(this))
        refreshLayout.setRefreshFooter(BallPulseFooter(this))
    }

    /**
     * 试用 RecyclerViewAdapterHelper
     */
    private fun testRecyclerViewAdapterHelper() {
        //测试数据
        actionList.add(Action("a"))
        actionList.add(Article("b"))
        actionList.add(Action("c"))
        actionAdapter?.addData(actionList)
        actionAdapter?.addChildClickViewIds(R.id.tv_action_name)
        //item动画
        actionAdapter?.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
        //空数据页面
        actionAdapter?.setEmptyView(R.layout.view_empty)
        //item点击事件，用于测试
        actionAdapter?.setOnItemClickListener { _, _, _ ->
            Toast.makeText(
                this,
                "ss",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 请求动作测试数据
     */
    private fun getActionData() {
        RetrofitHelper.instance.requestListData<Action>({
            RetrofitHelper.instance.getRapApi(RapApi::class.java).getActionList()
        }, {
            actionList.clear()
            it.data?.let { list -> actionList.addAll(list) }
            actionAdapter?.setNewData(null)
            actionAdapter?.setDiffNewData(actionList)
        }, {
            actionList.clear()
            actionAdapter?.setNewData(null)
            actionAdapter?.setDiffNewData(actionList)
        })
    }

    //双击退出
    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if (secondTime - lastClickTime > 2000) {
            Toast.makeText(
                this,
                this.resources.getString(R.string.double_click_exit),
                Toast.LENGTH_SHORT
            ).show()
            lastClickTime = secondTime
        } else {
            AppManager.instance.appExit()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }
}
