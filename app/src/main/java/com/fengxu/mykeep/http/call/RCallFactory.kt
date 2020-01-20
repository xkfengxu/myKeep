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
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //获取原始类型
        val rawType = getRawType(returnType)
        //返回值必须是Custom并且带有泛型（参数类型），根据APIService接口中的方法返回值，确定returnType
        //如 CustomCall<String> getCategories();，那确定returnType就是CustomCall<String>
        if (rawType == RCall::class.java && returnType is ParameterizedType) {
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