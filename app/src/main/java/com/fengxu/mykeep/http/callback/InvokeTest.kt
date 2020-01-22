package com.fengxu.mykeep.http.callback

import com.fengxu.mykeep.bean.Action

/**
 * @author fengxu
 * @createDate 2020 01 22
 * @description
 */
open class InvokeTest {

    fun getAction(parameter: String): Action {
        return Action("sss")
    }
}