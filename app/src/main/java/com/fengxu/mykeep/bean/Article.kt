package com.fengxu.mykeep.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.fengxu.mykeep.adapter.CommonAdapter

/**
 * @author fengxu
 * 文章
 */
class Article(
    //标题
    var title: String?
) : MultiItemEntity {

    override val itemType: Int
        get() = CommonAdapter.TYPE_ARTICLE

    /**
     *  内容
     */
    var content: String = ""
    /**
     * 插图
     */
    var illustration: String = ""
}