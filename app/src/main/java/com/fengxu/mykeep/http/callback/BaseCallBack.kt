package com.fengxu.mykeep.http.callback

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.fengxu.mykeep.http.response.BaseResponse
import com.fengxu.mykeep.utils.FileUtil.saveValueToJsonFile

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description T为传入类型：对象、列表等，R为解析类，可以理解为适配器
 */
open class BaseCallBack<T>() {

    /**
     * 缓存文件名
     */
    var cache: String? = null

    /**
     * 可选择缓存或不进行缓存的构造方法
     */
    constructor(cache: String?) : this() {
        this.cache = cache
    }

    /**
     * UI Thread
     * 收到返回后的回调
     */
    open fun onAfter() {}

    open fun onResponse(response: T) {}

    open fun onError(response: BaseResponse) {}

    open fun onProgress(progress: Float) {}

    @Suppress("UNCHECKED_CAST")
    open fun <R> parseNetworkResponse(response: R): T? {
        try {
            val baseResponse = response as T
            //可根据Response添加判断，请求错误时不进行缓存

            if (!TextUtils.isEmpty(cache)) {
                saveValueToJsonFile(cache, JSON.toJSONString(baseResponse))
            }
            return baseResponse
        } catch (e: Exception) {
            //可记录log
            e.printStackTrace()
        }
        return null
    }
}