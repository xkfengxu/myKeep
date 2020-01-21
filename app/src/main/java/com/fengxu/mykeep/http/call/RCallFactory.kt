package com.fengxu.mykeep.http.call

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 自定义call工厂类
 */
class RCallFactory : CallAdapter.Factory() {
    override fun get(
        //根据APIService接口中的方法返回值，确定returnType
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //获取原始类型
        val rawType = getRawType(returnType)
        //返回值原始类型必须是我们自定义RCall
        //ParameterizedType检查是否参数化类型
        //例子： List<String> list1;  变量：list1     RawType：java.util.List（变量类型）   类型：java.lang.String（参数类型）
        if (rawType == RCall::class.java && returnType is ParameterizedType) {
            //获取参数上限 调用getActualTypeArguments获得所有参数类型 按index获取 如果是 WildcardType （泛型表达式）则获取上界
            //补充WildcardType 如果不存在显式声明的上边界，则上边界为 Object。如果不存在显式声明的下边界，则下边界为类型 null
            val callReturnType = getParameterUpperBound(0, returnType)
            return object : CallAdapter<Any, RCall<Any>> {
                override fun responseType(): Type {
                    return callReturnType
                }

                override fun adapt(call: Call<Any>): RCall<Any> {
                    return RCall(call)
                }
            }
        }
        return null
    }
}