package com.fengxu.mykeep.http.response

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 数据返回基类
 */
open class BaseResponse {
    var code: Int = -1
    var msg: String? = null
}