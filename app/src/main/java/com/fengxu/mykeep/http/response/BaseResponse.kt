package com.fengxu.mykeep.http.response

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 数据返回基类
 */
open class BaseResponse {
    val code: Int = -1
    val msg: String? = null
}