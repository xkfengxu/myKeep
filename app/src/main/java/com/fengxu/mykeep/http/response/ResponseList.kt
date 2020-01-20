package com.fengxu.mykeep.http.response

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 返回列表数据
 */
class ResponseList<T> : BaseResponse() {
    var data: List<T>? = null
}