package com.fengxu.mykeep.utils

import com.fengxu.mykeep.MyApplication
import java.io.*
import java.nio.ByteBuffer

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description 文件操作工具类
 * 用object 修饰的类为静态类，里面的方法和变量都为静态的。
 */
@Deprecated("用MMKV替换")
object FileUtil {

    private const val JSON_DIR_NAME = "app_json"

    /**
     * 同步：保存json串到文件中
     */
    @Deprecated("用MMKV替换")
    fun saveValueToJsonFile(key: String?, value: String): Boolean {
        val jsonFile: File = key?.let { getJsonFile(it) } ?: return false
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(jsonFile)
            val fc = fos.channel
            val buffer = ByteBuffer.wrap(value.toByteArray())
            buffer.put(value.toByteArray())
            buffer.flip()
            fc.write(buffer)
            fc.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            close(fos)
        }
        return false
    }

    /**
     * 使用NIO获取json串
     */
    @Deprecated("用MMKV替换")
    fun getValueFromJsonFile(key: String?): String? {
        val jsonFile: File? = key?.let { getJsonFile(it) }
        if (jsonFile == null || !jsonFile.exists()) {
            return Key.NIL
        }
        var fis: FileInputStream? = null
        val bao = ByteArrayOutputStream()
        return try {
            fis = FileInputStream(jsonFile)
            val fc = fis.channel
            val buffer = ByteBuffer.allocate(1 shl 13)
            var j: Int
            while (fc.read(buffer).also { j = it } != -1) {
                buffer.flip()
                bao.write(buffer.array(), 0, j)
                buffer.clear()
            }
            bao.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            Key.NIL
        } finally {
            close(fis)
            close(bao)
        }
    }

    /**
     * 获取json文件
     *
     * @param key 文件名
     */
    private fun getJsonFile(key: String): File? {
        val fileDirName = MyApplication.CONTEXT.cacheDir?.parent + File.separator + JSON_DIR_NAME
        val parentFile = File(fileDirName)
        var file: File? = null
        if (parentFile.exists()) {
            file = File(parentFile, key)
        } else {
            if (parentFile.mkdir()) {
                file = File(parentFile, key)
            }
        }
        return file
    }

    /**
     * Closeable是可以关闭的数据的源或目标。调用close方法来释放对象所持有的资源(例如打开的文件)
     */
    private fun close(vararg closeables: Closeable?) {
        try {
            for (closeable in closeables) {
                if (closeable == null) {
                    continue
                }
                closeable.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}