package com.fengxu.mykeep.activity

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.fengxu.mykeep.R
import com.fengxu.mykeep.adapter.CommonAdapter
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.bean.Action
import com.fengxu.mykeep.bean.Article
import com.fengxu.mykeep.http.RetrofitHelper
import com.fengxu.mykeep.http.api.RapApi
import com.fengxu.mykeep.utils.FileUtil
import com.fengxu.mykeep.utils.FileUtil.getValueFromJsonFile
import com.fengxu.mykeep.utils.Key
import com.fengxu.mykeep.widget.BannerView
import com.fengxu.mykeep.widget.banner.CircleIndicator
import com.scwang.smartrefresh.header.FunGameBattleCityHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter

class MainActivity : BaseActivity() {

    private var adapter: CommonAdapter? = CommonAdapter(null)
    private val actionList = ArrayList<MultiItemEntity>()
    private val bannerUrl: MutableList<String> = ArrayList()
    private var mBannerView: BannerView? = null

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun intiView() {
        findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            showFloatView(this.localClassName)
            getData()
        }
        val lottie = findViewById<LottieAnimationView>(R.id.animation_view)
        //recyclerView 设置layoutManager，adapter
        val recyclerView = findViewById<RecyclerView>(R.id.rl_video)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        testRecyclerViewAdapterHelper()
        testSmartRefreshLayout()
        setLottieAnimationView(lottie)
        getBannerUrl()
    }

    /**
     * 请求测试数据
     */
    private fun getData() {
        RetrofitHelper.instance.requestListData<Action>({
            RetrofitHelper.instance.getRapApi(RapApi::class.java).getActionList()
        }, {
            actionList.clear()
            it.data?.let { list -> actionList.addAll(list) }
            adapter?.setNewData(null)
            adapter?.setDiffNewData(actionList)
        }, {
            actionList.clear()
            adapter?.setNewData(null)
            adapter?.setDiffNewData(actionList)
        })
    }

    /**
     * 请求轮播图
     */
    @Suppress("UNCHECKED_CAST")
    private fun getBannerUrl() {
        val cacheUrlString = getValueFromJsonFile(Key.BANNER_URL)
        if (mBannerView == null) {
            mBannerView = findViewById(R.id.banner_view)
            mBannerView!!.setIndicator(CircleIndicator(applicationContext))
            if (!TextUtils.isEmpty(cacheUrlString)) {
                bannerUrl.addAll(JSONArray.parseArray(cacheUrlString) as List<String>)
                mBannerView?.setBannerData(bannerUrl)
            }
        }
        RetrofitHelper.instance.requestListData<String>({
            RetrofitHelper.instance.getRapApi(RapApi::class.java).getBanner()
        }, {
            it.data?.let { list ->
                val jsonUrl  = JSON.toJSONString(it.data)
                if (jsonUrl != cacheUrlString) {
                    FileUtil.saveValueToJsonFile(Key.BANNER_URL, jsonUrl)
                    bannerUrl.clear()
                    bannerUrl.addAll(list)
                    mBannerView!!.setIndicator(CircleIndicator(applicationContext))
                    mBannerView?.setBannerData(bannerUrl)
                }
            }
        }, {})
    }

    /**
     * Lottie使用个小技巧
     */
    private fun setLottieAnimationView(lottie: LottieAnimationView) {
        //动画监听
        lottie.addAnimatorListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                lottie.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        //倒放
        lottie.speed = (-1).toFloat()
        //当你设置-1的时候就代表无限循环
        lottie.repeatCount = -1
    }

    /**
     * 试用 SmartRefreshLayout
     * 设置刷新，加载时长
     * Header，Footer 可在代码或xml中配置，代码中优先级最高
     */
    private fun testSmartRefreshLayout() {
        val refreshLayout = findViewById<SmartRefreshLayout>(R.id.rf_video) as RefreshLayout
        refreshLayout.setOnRefreshListener { refreshlayout -> refreshlayout.finishRefresh(2000); }
        refreshLayout.setOnLoadMoreListener { refreshlayout -> refreshlayout.finishLoadMore(2000); }
        refreshLayout.setRefreshHeader(FunGameBattleCityHeader(this))
        refreshLayout.setRefreshFooter(BallPulseFooter(this))
    }

    /**
     * 试用 RecyclerViewAdapterHelper
     */
    private fun testRecyclerViewAdapterHelper() {
        actionList.add(Action("a"))
        actionList.add(Article("b"))
        actionList.add(Action("c"))
        adapter?.addData(actionList)
        adapter?.addChildClickViewIds(R.id.tv_action_name)
        //item动画
        adapter?.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
        //空数据页面
        adapter?.setEmptyView(R.layout.view_empty)
        adapter?.setOnItemClickListener { _, _, _ ->
            Toast.makeText(
                this,
                "ss",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
