package com.fengxu.mykeep.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author fengxu
 * @createDate 2020 01 20
 * @description MD5加密工具类
 */
object MD5 {
    fun md5(string: String): String {
        val hash: ByteArray
        hash = try {
            MessageDigest.getInstance("MD5")
                .digest(string.toByteArray(StandardCharsets.UTF_8))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, MD5 should be supported?", e)
        }
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (Integer.toHexString(0xFF and b.toInt()).length == 1) {
                hex.append("0").append(Integer.toHexString(0xFF and b.toInt()))
            } else {
                hex.append(Integer.toHexString(0xFF and b.toInt()))
            }
        }
        return hex.toString()
    }
}