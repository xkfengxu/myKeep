package com.fengxu.mykeep.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fengxu.mykeep.R
import com.fengxu.mykeep.bean.Action

/**
 * @author fengxu
 */
class ActionAdapter(data: MutableList<Action>?) :
    BaseQuickAdapter<Action, BaseViewHolder>(R.layout.item_action, data) {


    override fun convert(helper: BaseViewHolder, item: Action?) {
        helper.setText(R.id.tv_action_name, item?.name)
    }
}