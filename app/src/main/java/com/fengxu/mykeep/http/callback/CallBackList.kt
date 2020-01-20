package com.fengxu.mykeep.http.callback

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.fengxu.mykeep.http.response.ResponseList
import com.fengxu.mykeep.utils.FileUtil.saveValueToJsonFile

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 列表回调
 */
open class CallBackList<T>() : BaseCallBack<List<T>>() {

    constructor(cache: String) : this() {
        this.cache = cache
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R> parseNetworkResponse(response: R): List<T>? {
        try {
            val baseResponse: ResponseList<T> = response as ResponseList<T>
            //可根据Response添加判断，请求错误时不进行缓存
            if (!TextUtils.isEmpty(cache)) {
                saveValueToJsonFile(cache, JSON.toJSONString(response))
            }
            return baseResponse.data
        } catch (e: Exception) {
            //可记录log
            e.printStackTrace()
        }
        return null
    }
}