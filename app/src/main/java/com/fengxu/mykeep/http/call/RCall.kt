package com.fengxu.mykeep.http.call

import com.fengxu.mykeep.http.RetrofitHelper
import com.fengxu.mykeep.http.callback.BaseCallBack
import retrofit2.Call

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 自定义call-接受列表
 */
class RCall<R>() {
    private var call: Call<R>? = null

    constructor(call: Call<R>?) : this() {
        this.call = call
    }

    fun <T> execute(callback: BaseCallBack<T>?) {
//        RetrofitHelper.instance.execute(call, callback)
    }
}