package com.fengxu.mykeep.activity

import android.annotation.SuppressLint
import android.util.Rational
import android.view.WindowManager
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.Preview
import androidx.camera.core.impl.PreviewConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.fengxu.mykeep.R
import com.fengxu.mykeep.base.BaseActivity
import com.fengxu.mykeep.constant.RouteConstant
import java.io.File

@Route(path = RouteConstant.ACTIVITY_CAMERA)
class CameraActivity : BaseActivity() {

    override fun intiView() {
        //改变状态栏颜色，API LEVEL21
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        //CameraView设置
        val camera:CameraView = findViewById(R.id.camera);
        camera.bindToLifecycle(this)
    }


    @SuppressLint("RestrictedApi")
    override fun onDestroy() {
        super.onDestroy()
        CameraX.unbindAll()
    }

    override fun getContentView(): Int {
        return R.layout.activity_camera
    }
}