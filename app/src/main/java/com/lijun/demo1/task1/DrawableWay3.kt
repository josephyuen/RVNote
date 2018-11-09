package com.lijun.demo1.task1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.lijun.demo1.R

/**
 * Creator: yiming
 * FuncDesc:  自定义View 绘制图片
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
class DrawableWay3: View {

    constructor(context: Context?) : super(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

    }


    private lateinit var bitmap: Bitmap
    val paint = Paint()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap,0f,0f,paint)

    }

}
