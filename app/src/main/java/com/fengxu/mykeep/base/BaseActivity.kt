package com.fengxu.mykeep.base

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import com.fengxu.mykeep.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference

/**
 * @author fengxu
 */
abstract class BaseActivity : AppCompatActivity() {

    private var contentView: ViewGroup? = null
    private var floatView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.instance.pushActivity(WeakReference(this))
        beforeViews()
        setContentView(getContentView())
        contentView =
            this.window?.decorView?.rootView?.findViewById(android.R.id.content)
        floatView = this.layoutInflater.inflate(R.layout.view_float, null)
        intiView()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.popActivity()
    }

    abstract fun intiView()

    abstract fun getContentView(): Int

    open fun beforeViews() {}

    /**
     * 在屏幕上添加一个view，展示一些信息
     */
    @SuppressLint("InflateParams")
    fun showFloatView(fileName: String) {
        if (floatView?.let { contentView?.contains(it) }!!) {
            contentView?.removeView(floatView)
            return
        }
        val value = readPageInfo(fileName)
        if (TextUtils.isEmpty(value)) {
            return
        }
        contentView?.addView(floatView)
        floatView!!.findViewById<TextView>(R.id.tv_function_info).text = value
    }

    private fun readPageInfo(fileName: String): String {
        val content = StringBuilder("")
        try {
            val instream = resources.assets.open(fileName)
            val buffreader = BufferedReader(InputStreamReader(instream))
            var line: String
            while (buffreader.readLine().also { line = it } != null) {
                content.append(line + "\n")
            }
            instream.close()
        } catch (e: Exception) {
        }
        return content.toString()
    }
}