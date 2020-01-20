package com.fengxu.mykeep.http.response

/**
 * @author fengxu
 * @createDate 2020 01 19
 * @description 返回单个对象
 */
class ResponseData<T>: BaseResponse() {
    private val data: T? = null
}