package com.fengxu.mykeep.bean

/**
 * @author fengxu
 * 动作
 */
class Action(
    /**
     * 动作名称
     */
    var name: String?
) {

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