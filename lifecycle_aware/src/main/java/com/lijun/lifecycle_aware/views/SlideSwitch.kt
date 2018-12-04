package com.lijun.lifecycle_aware.views

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.lijun.lifecycle_aware.R

/**
 * Creator: yiming
 * Date: 2018/12/4 4:49 PM
 * FuncDesc:
 * copyright  ©2018-2020 Ququ Technology Corporation. All rights reserved.
 */
class SlideSwitch : View {

    //用于显示的文本
    private var mOnText = "打开"
    private var mOffText = "关闭"

    private var mSwitchStatus = SWITCH_OFF

    private var mHasScrolled = false//表示是否发生过滚动

    private var mSrcX = 0
    private var mDstX = 0

    private var mBmpWidth = 0
    private var mBmpHeight = 0
    private var mThumbWidth = 0

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mOnSwitchChangedListener: OnSwitchChangedListener? = null

    //开关状态图
    private lateinit var mSwitch_off: Bitmap
    private lateinit var mSwitch_on: Bitmap
    private lateinit var mSwitch_thumb: Bitmap

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    //初始化三幅图片
    private fun init() {
        val res = resources
//        mSwitch_off = BitmapFactory.decodeResource(res, R.drawable.bg_switch_off)
//        mSwitch_on = BitmapFactory.decodeResource(res, R.drawable.bg_switch_on)
//        mSwitch_thumb = BitmapFactory.decodeResource(res, R.drawable.switch_thumb)
        mBmpWidth = mSwitch_on.width
        mBmpHeight = mSwitch_on.height
        mThumbWidth = mSwitch_thumb.width
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        params.width = mBmpWidth
        params.height = mBmpHeight
        super.setLayoutParams(params)
    }

    /**
     * 为开关控件设置状态改变监听函数
     * @param onSwitchChangedListener 参见 [OnSwitchChangedListener]
     */
    fun setOnSwitchChangedListener(onSwitchChangedListener: OnSwitchChangedListener) {
        mOnSwitchChangedListener = onSwitchChangedListener
    }

    /**
     * 设置开关上面的文本
     * @param onText  控件打开时要显示的文本
     * @param offText  控件关闭时要显示的文本
     */
    fun setText(onText: String, offText: String) {
        mOnText = onText
        mOffText = offText
        invalidate()
    }

    /**
     * 设置开关的状态
     * @param on 是否打开开关 打开为true 关闭为false
     */
    fun setStatus(on: Boolean) {
        mSwitchStatus = if (on) SWITCH_ON else SWITCH_OFF
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        Log.d(TAG, "onTouchEvent  x=" + event.x)
        when (action) {
            MotionEvent.ACTION_DOWN -> mSrcX = event.x.toInt()
            MotionEvent.ACTION_MOVE -> {
                mDstX = Math.max(event.x.toInt(), 10)
                mDstX = Math.min(mDstX, 62)
                if (mSrcX == mDstX)
                    return true
                mHasScrolled = true
                val aTransRunnable = AnimationTransRunnable(mSrcX.toFloat(), mDstX.toFloat(), 0)
                Thread(aTransRunnable).start()
                mSrcX = mDstX
            }
            MotionEvent.ACTION_UP -> {
                if (mHasScrolled == false)
                //如果没有发生过滑动，就意味着这是一次单击过程
                {
                    mSwitchStatus = Math.abs(mSwitchStatus - 1)
                    var xFrom = 10
                    var xTo = 62
                    if (mSwitchStatus == SWITCH_OFF) {
                        xFrom = 62
                        xTo = 10
                    }
                    val runnable = AnimationTransRunnable(xFrom.toFloat(), xTo.toFloat(), 1)
                    Thread(runnable).start()
                } else {
                    invalidate()
                    mHasScrolled = false
                }
                //状态改变的时候 回调事件函数
                if (mOnSwitchChangedListener != null) {
                    mOnSwitchChangedListener!!.onSwitchChanged(this, mSwitchStatus)
                }
            }

            else -> {
            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘图的时候 内部用到了一些数值的硬编码，其实不太好，
        //主要是考虑到图片的原因，图片周围有透明边界，所以要有一定的偏移
        //硬编码的数值只要看懂了代码，其实可以理解其含义，可以做相应改进。
        mPaint.textSize = 14f
        mPaint.typeface = Typeface.DEFAULT_BOLD

        when (mSwitchStatus) {
            SWITCH_OFF -> {
                drawBitmap(canvas, null, null, mSwitch_off)
                drawBitmap(canvas, null, null, mSwitch_thumb)
                mPaint.color = Color.rgb(105, 105, 105)
                canvas.translate(mSwitch_thumb.width.toFloat(), 0f)
                canvas.drawText(mOffText, 0f, 20f, mPaint)
            }
            SWITCH_ON -> {
                drawBitmap(canvas, null, null, mSwitch_on)
                val count = canvas.save()
                canvas.translate((mSwitch_on.width - mSwitch_thumb.width).toFloat(), 0f)
                drawBitmap(canvas, null, null, mSwitch_thumb)
                mPaint.color = Color.WHITE
                canvas.restoreToCount(count)
                canvas.drawText(mOnText, 17f, 20f, mPaint)
            }
            else
                //SWITCH_SCROLING
            -> {
                mSwitchStatus = if (mDstX > 35) SWITCH_ON else SWITCH_OFF
                drawBitmap(canvas, Rect(0, 0, mDstX, mBmpHeight), Rect(0, 0, mDstX, mBmpHeight), mSwitch_on)
                mPaint.color = Color.WHITE
                canvas.drawText(mOnText, 17f, 20f, mPaint)

                var count = canvas.save()
                canvas.translate(mDstX.toFloat(), 0f)
                drawBitmap(
                    canvas, Rect(mDstX, 0, mBmpWidth, mBmpHeight),
                    Rect(0, 0, mBmpWidth - mDstX, mBmpHeight), mSwitch_off
                )
                canvas.restoreToCount(count)

                count = canvas.save()
                canvas.clipRect(mDstX, 0, mBmpWidth, mBmpHeight)
                canvas.translate(mThumbWidth.toFloat(), 0f)
                mPaint.color = Color.rgb(105, 105, 105)
                canvas.drawText(mOffText, 0f, 20f, mPaint)
                canvas.restoreToCount(count)

                count = canvas.save()
                canvas.translate((mDstX - mThumbWidth / 2).toFloat(), 0f)
                drawBitmap(canvas, null, null, mSwitch_thumb)
                canvas.restoreToCount(count)
            }
        }

    }

    fun drawBitmap(canvas: Canvas, src: Rect?, dst: Rect?, bitmap: Bitmap) {
        var dst = dst
        dst = dst ?: Rect(0, 0, bitmap.width, bitmap.height)
        val paint = Paint()
        canvas.drawBitmap(bitmap, src, dst, paint)
    }

    /**
     * AnimationTransRunnable 做滑动动画所使用的线程
     */
    private inner class AnimationTransRunnable
    /**
     * 滑动动画
     * @param srcX 滑动起始点
     * @param dstX 滑动终止点
     * @param duration 是否采用动画，1采用，0不采用
     */
        (srcX: Float, dstX: Float, private val duration: Int) : Runnable {
        private val srcX: Int
        private val dstX: Int

        init {
            this.srcX = srcX.toInt()
            this.dstX = dstX.toInt()
        }

        override fun run() {
            val patch = if (dstX > srcX) 5 else -5
            if (duration == 0) {
                this@SlideSwitch.mSwitchStatus = SWITCH_SCROLING
                this@SlideSwitch.postInvalidate()
            } else {
                Log.d(TAG, "start Animation: [ $srcX , $dstX ]")
                var x = srcX + patch
                while (Math.abs(x - dstX) > 5) {
                    mDstX = x
                    this@SlideSwitch.mSwitchStatus = SWITCH_SCROLING
                    this@SlideSwitch.postInvalidate()
                    x += patch
                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
                mDstX = dstX
                this@SlideSwitch.mSwitchStatus = if (mDstX > 35) SWITCH_ON else SWITCH_OFF
                this@SlideSwitch.postInvalidate()
            }
        }

    }

    interface OnSwitchChangedListener {
        /**
         * 状态改变 回调函数
         * @param status  SWITCH_ON表示打开 SWITCH_OFF表示关闭
         */
        fun onSwitchChanged(obj: SlideSwitch, status: Int)
    }

    companion object {
        val TAG = "SlideSwitch"
        val SWITCH_OFF = 0//关闭状态
        val SWITCH_ON = 1//打开状态
        val SWITCH_SCROLING = 2//滚动状态
    }

}
