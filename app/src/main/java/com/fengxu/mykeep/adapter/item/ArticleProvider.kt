package com.fengxu.mykeep.adapter.item

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fengxu.mykeep.R
import com.fengxu.mykeep.adapter.CommonAdapter
import com.fengxu.mykeep.bean.Article

/**
 * @author fengxu
 */
class ArticleProvider : BaseItemProvider<MultiItemEntity>() {

    override val itemViewType: Int
        get() = CommonAdapter.TYPE_ARTICLE

    override val layoutId: Int
        get() = R.layout.item_article

    override fun convert(helper: BaseViewHolder, data: MultiItemEntity?) {
        val article = data as Article
        helper.setText(R.id.tv_article_title, article.title)
    }
}