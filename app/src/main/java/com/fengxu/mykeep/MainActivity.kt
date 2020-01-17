package com.fengxu.mykeep

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fengxu.mykeep.adapter.ActionAdapter
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.bean.Action
import com.scwang.smartrefresh.header.FunGameBattleCityHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter


class MainActivity : BaseActivity() {

    var adapter: ActionAdapter? = ActionAdapter(null)

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun intiView() {
        findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            showFloatView(this.localClassName)
            val actionList = ArrayList<Action>()
            actionList.add(Action("s"))
            actionList.add(Action("2"))
            actionList.add(Action("1"))
            adapter?.setNewData(actionList)
        }
        val lottie = findViewById<LottieAnimationView>(R.id.animation_view)
        //recyclerView 设置layoutManager，adapter
        val recyclerView = findViewById<RecyclerView>(R.id.rl_video)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        testRecyclerViewAdapterHelper()
        testSmartRefreshLayout()
        setLottieAnimationView(lottie)
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
        val actionList = ArrayList<Action>()
        actionList.add(Action("a"))
        actionList.add(Action("b"))
        actionList.add(Action("c"))
        adapter?.setNewData(actionList)
        adapter?.addChildClickViewIds(R.id.tv_action_name)
        //item动画
        adapter?.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
        adapter?.setOnItemClickListener { _, _, _ ->
            Toast.makeText(
                this,
                "ss",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
