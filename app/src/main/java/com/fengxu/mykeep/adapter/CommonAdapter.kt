package com.fengxu.mykeep.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.fengxu.mykeep.adapter.item.ActionProvider
import com.fengxu.mykeep.adapter.item.ArticleProvider


/**
 * @author fengxu
 */
class CommonAdapter(data: MutableList<MultiItemEntity>?) :
    BaseProviderMultiAdapter<MultiItemEntity>(data) {

    companion object {
        const val TYPE_ACTION = 1
        const val TYPE_ARTICLE = 2
    }

    init {
        addItemProvider(ActionProvider())
        addItemProvider(ArticleProvider())
    }

    override fun getItemType(data: List<MultiItemEntity>, position: Int): Int {
        return data.get(position).itemType
    }
}