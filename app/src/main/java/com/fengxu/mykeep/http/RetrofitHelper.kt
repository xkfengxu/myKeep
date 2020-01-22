package com.fengxu.mykeep.http


import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fengxu.mykeep.http.interceptor.CommonInterceptor
import com.fengxu.mykeep.http.response.BaseResponse
import com.fengxu.mykeep.http.response.ResponseList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 网络请求配置工具
 */
class RetrofitHelper : ViewModel() {

    /**
     * 静态内部类单例
     */
    companion object {
        val instance = HelperHolder.holder
    }

    private object HelperHolder {
        val holder = RetrofitHelper()
    }

    /**
     * 记录业务的Api类
     */
    private val map = ArrayMap<Class<*>, Any>()

    /**
     * 这里的写法是抄公司里的业务场景
     * 有多个域名，将不同的域名及对应的api放在同一个类，用反射的方式获取类中的请求域名，方便域名的切换
     * 当有不同公共参数时，构造不同的拦截器链调用 getApi
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getRapApi(tClass: Class<T>?): T {
        var api = map[tClass] as T
        if (api == null) {
            val interceptors: MutableList<Interceptor> =
                ArrayList()
            interceptors.add(CommonInterceptor())
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            interceptors.add(logging)
            api = getApi(tClass, interceptors)
        }
        return api
    }

    @Synchronized
    private fun <T> getApi(
        tClass: Class<T>?,
        interceptorList: List<Interceptor>
    ): T {
        //okhttp构建者
        val okBuilder = OkHttpClient.Builder()
            //连接超时时长，同理可设置readTimeout writeTimeout
            .connectTimeout(15, TimeUnit.SECONDS)
        //okhttp拦截器链
        okBuilder.interceptors().addAll(interceptorList)
        //retrofit构建者
        val builder = Retrofit.Builder()
            //构建成用于请求的HTTP客户端
            .client(okBuilder.build())
            //添加一个呼叫适配器工厂以支持{@link * Call}以外的服务方法返回类型。
//            .addCallAdapterFactory(RCallFactory())
            //添加转换器工厂用于对象的序列化和反序列化,还有guava、jackson、java8、jaxb、moshi、protobuf、scalars、simplexml、wire 等Converter
            .addConverterFactory(GsonConverterFactory.create())
        try {
            //用反射的方式获取类中的请求域名，方便域名的切换
            val fieldd = tClass!!.getDeclaredField("API")
            //如果field的name是一个static的变量，field.get(param)，param是任意的都可以，返回类中当前静态变量的值。如果是非静态变量，field.get(obj)，obj必须是当前类的实例对象，返回实例对象obj的变量值。
            val apiStr = fieldd[null] as String
            val api = builder.baseUrl(apiStr).build().create(tClass)
            map[tClass] = api
            return api
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        throw UnsupportedOperationException("cannot be instantiated")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> requestListData(
        request: suspend CoroutineScope.() -> BaseResponse,
        success: ((info: ResponseList<T>) -> Unit),
        error: ((info: BaseResponse) -> Unit)
    ) {
        apiCall(request, success as (BaseResponse) -> Unit, error, null)
    }

    /**
     * 请求执行类
     */
    @Suppress("UNCHECKED_CAST")
    fun apiCall(
        request: suspend CoroutineScope.() -> BaseResponse,
        success: ((info: BaseResponse) -> Unit),
        error: ((info: BaseResponse) -> Unit),
        after: (() -> Unit)?
    ) {
        viewModelScope.launch {
            val response = withContext(IO) {
                coroutineScope {
                    try {
                        //call?.execute()
                        //invoke 接口只有一个方法的时候 内部将接口给实例化,逻辑通过invoke方法代理出去
                        //request是匿名函数，invoke表示为通过函数变量调用自身
                        //还不是很懂
                        return@coroutineScope request.invoke(this)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        val response = BaseResponse()
                        //可自定义处理  如缺少转换器报错之类会在此被捕获
                        response.code = -1
                        response.msg = e.message
                        return@coroutineScope response
                    }
                }
            }
            if (response.code == 0) {
                success(response)
            } else {
                error(response)
            }
            after?.invoke()
        }
    }
}