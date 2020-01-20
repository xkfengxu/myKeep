package com.fengxu.mykeep.http.converterfactory

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 转换器 将String转换为request
 */
class StringRequestBodyConverter : Converter<String, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: String?): RequestBody? {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"))
        writer.write(value)
        writer.close()
        return buffer.readByteString()
            .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull()!!)
    }
}