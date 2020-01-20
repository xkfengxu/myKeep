package com.fengxu.mykeep.http.api

import com.fengxu.mykeep.bean.Action
import com.fengxu.mykeep.http.call.RCall
import com.fengxu.mykeep.http.response.ResponseList
import retrofit2.http.GET

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description  http://rap2.taobao.org/ RAP2在线模拟假数据,实现前后端分离  相似工具 YApi
 */
interface RapApi {

    companion object {
        const val API = "http://rap2api.taobao.org/"
    }

    /**
     * 获取动作列表
     */
    @GET("app/mock/243232/getActionList")
    fun getActionList(): RCall<ResponseList<Action>>

    /**
     * 获取轮播图
     */
    @GET("app/mock/243232/getBanner")
    fun getBanner(): RCall<ResponseList<String>>

}