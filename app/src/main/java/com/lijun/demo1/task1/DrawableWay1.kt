package com.lijun.demo1.task1

import android.content.Context
import android.widget.ImageView
import com.lijun.demo1.R

/**
 * Creator: yiming
 * FuncDesc: 使用 ImageView 绘制一张图片
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
class DrawableWay1{

    fun setDrawable(context: Context){
        val imageView = ImageView(context)
        val params = imageView.layoutParams
        params.width = 400
        params.height = 300
        imageView.layoutParams = params
        imageView.setImageResource(R.mipmap.ic_launcher)
    }

}
