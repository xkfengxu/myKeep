package com.fengxu.mykeep.adapter.item

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fengxu.mykeep.R
import com.fengxu.mykeep.adapter.CommonAdapter
import com.fengxu.mykeep.bean.Action

/**
 * @author fengxu
 */
class ActionProvider : BaseItemProvider<MultiItemEntity>() {

    override val itemViewType: Int
        get() = CommonAdapter.TYPE_ACTION

    override val layoutId: Int
        get() = R.layout.item_action

    override fun convert(helper: BaseViewHolder, data: MultiItemEntity?) {
        val action = data as Action
        helper.setText(R.id.tv_action_name, action.name)
    }
}