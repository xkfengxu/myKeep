package com.fengxu.mykeep.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.fengxu.mykeep.adapter.CommonAdapter

/**
 * @author fengxu
 * 动作
 */
class Action(
    //动作名称
    var name: String?
) : MultiItemEntity {

    override val itemType: Int
        get() = CommonAdapter.TYPE_ACTION
    /**
     * 动作说明
     */
    var description: String? = ""
    /**
     * 动作级别
     */
    var level: Int? = 0
    /**
     * 动作预览图
     */
    var preView: String? = ""
}