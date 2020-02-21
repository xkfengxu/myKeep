package com.fengxu.mykeep.arouter

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

/**
 * @author fengxu
 * arouter拦截器优先级越小越先执行，不可存在同优先级拦截器
 * 拦截器 - AOP
 * arouter 分组懒加载
 */

@Interceptor(priority = 1)
class UserIInterceptor : IInterceptor {
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        //可做自定义处理，如未登录统一跳转登录页面，继续在navigation中处理
        callback!!.onContinue(postcard)
    }

    override fun init(context: Context?) {
    }
}