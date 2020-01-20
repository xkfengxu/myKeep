package com.fengxu.mykeep.http.interceptor

import android.net.Uri
import android.text.TextUtils
import com.fengxu.mykeep.base.BaseApp
import com.fengxu.mykeep.base.BaseApp.Companion.SIGN
import com.fengxu.mykeep.utils.Key.KEY_SIGN
import com.fengxu.mykeep.utils.Key.VERSION
import com.fengxu.mykeep.utils.MD5.md5
import okhttp3.*
import java.io.IOException
import java.util.*

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 自定义拦截器 为请求添加公共参数生成签名
 */
class CommonInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        //公共参数
        val parameterMap: MutableMap<String, String> = HashMap()
        BaseApp.VERSION_NAME?.let { parameterMap.put(VERSION, it) }
        //post只需要更换新的body内容,get只需要更换新的url内容
        var httpUrl: HttpUrl? = oldRequest.url
        var body: RequestBody? = oldRequest.body
        //path参与签名校验不进行encode，不使用httpUrl.encodedPath()
        val path = Uri.parse(oldRequest.url.toString()).path
        //当请求body不为空且为表单形式，当作post请求处理签名,否则以get方式添加参数签名
        if (oldRequest.body != null && oldRequest.body is FormBody) {
            body = postFormBodyMethod(body as FormBody, path, parameterMap)
        } else {
            httpUrl = getMethod(httpUrl, path, parameterMap)
        }
        val newRequest: Request = oldRequest.newBuilder()
            .method(oldRequest.method, body)
            .url(httpUrl!!)
            .build()
        return chain.proceed(newRequest)
    }

    /**
     * 表单形式post请求
     */
    private fun postFormBodyMethod(
        oldBody: FormBody,
        path: String?,
        parameterMap: MutableMap<String, String>
    ): RequestBody? {
        val nameList =
            ArrayList(parameterMap.keys)
        //原请求中body的内容也需要参与签名校验
        for (i in 0 until oldBody.size) {
            parameterMap[oldBody.name(i)] = oldBody.value(i)
            nameList.add(oldBody.name(i))
        }
        //重新拼接body内容
        val bodyBuilder = FormBody.Builder()
        for ((key, value) in parameterMap) {
            bodyBuilder.add(key, value)
        }
        //body添加签名参数
        bodyBuilder.add(KEY_SIGN, getSign(path, nameList, parameterMap))
        return bodyBuilder.build()
    }

    /**
     * get请求
     */
    private fun getMethod(
        oldRequest: HttpUrl?,
        path: String?,
        parameterMap: MutableMap<String, String>
    ): HttpUrl? {
        val nameList =
            ArrayList(parameterMap.keys)
        val urlBuilder = oldRequest?.newBuilder()
        //使用原请求，url添加公共参数
        for ((key, value) in parameterMap) {
            urlBuilder?.addQueryParameter(key, value)
        }
        //原请求中url后拼接的参数也需要参与签名校验
        if (oldRequest?.queryParameterNames != null && oldRequest.queryParameterNames.isNotEmpty()) {
            for (name in oldRequest.queryParameterNames) {
                nameList.add(name)
                parameterMap[name] = oldRequest.queryParameter(name)!!
            }
        }
        //url添加签名参数
        urlBuilder?.addQueryParameter(KEY_SIGN, getSign(path, nameList, parameterMap))
        return urlBuilder?.build()
    }

    /**
     * 根据参数生成并添加签名
     */
    private fun getSign(
        path: String?,
        nameList: ArrayList<String>,
        parameterMap: Map<String, String>
    ): String {
        nameList.sortWith(Comparator { obj: String, s: String? ->
            obj.compareTo(
                s!!
            )
        })
        var buffer = StringBuilder()
        for (s in nameList) {
            if (TextUtils.isEmpty(s)) {
                continue
            }
            buffer = buffer.append("&")
                .append(s)
                .append("=")
                .append(parameterMap[s])
        }
        return md5(path + "?" + buffer.substring(1) + SIGN)
    }
}