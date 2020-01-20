package com.fengxu.mykeep.http.converterfactory

import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 转换器 将response转换为String
 */
class StringResponseBodyConverter : Converter<ResponseBody, String> {
    override fun convert(value: ResponseBody): String? {
        return value.use { body ->
            body.string()
        }
    }
}