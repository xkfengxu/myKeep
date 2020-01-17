package com.fengxu.mykeep

import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fengxu.mykeep.adapter.ActionAdapter
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.bean.Action


class MainActivity : BaseActivity() {

    var adapter: ActionAdapter? = null

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun intiView() {
        findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            showFloatView(this.localClassName)
            val actionList = ArrayList<Action>()
            actionList.add(Action("1"))
            actionList.add(Action("2"))
            actionList.add(Action("3"))
            adapter?.setNewData(actionList)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.rl_video)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ActionAdapter(null)
        recyclerView.adapter = adapter
        testRecyclerViewAdapterHelper()
    }

    private fun testRecyclerViewAdapterHelper() {
        val actionList = ArrayList<Action>()
        actionList.add(Action("a"))
        actionList.add(Action("b"))
        actionList.add(Action("c"))
        adapter?.setNewData(actionList)
        adapter?.addChildClickViewIds(R.id.tv_action_name)
        adapter?.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
        adapter?.setOnItemClickListener { adapter, view, position ->
            Toast.makeText(
                this,
                "ss",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
