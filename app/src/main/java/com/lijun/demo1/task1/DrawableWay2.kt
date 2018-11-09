package com.lijun.demo1.task1

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.lijun.demo1.R
import kotlinx.android.synthetic.main.drawable_way2.*


/**
 * Creator: yiming
 * FuncDesc:  使用 surfaceView 绘制一张图片
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
class DrawableWay2: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawable_way2)
        buildDrawable()

    }

    private fun buildDrawable() {
        surface.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
                if (holder == null) {
                    return
                }
                val paint = Paint()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE

                val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
                val canvas = holder.lockCanvas()   // 获得 surfaceView 的 canvas
                canvas.drawBitmap(bitmap, 0f, 0f, paint)
                holder.unlockCanvasAndPost(canvas)
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {

            }

            override fun surfaceCreated(holder: SurfaceHolder?) {

            }
        })


    }

}
