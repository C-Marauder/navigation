package com.example.navigation

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View

class IndicatorView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    private val mPaint: Paint by lazy {
        Paint().apply {
            this.isAntiAlias = true
            this.color = Color.RED
            this.style = Paint.Style.STROKE
            this.strokeWidth = 10f
        }
    }
    private val mPath: Path by lazy {
        Path().apply {
            this.moveTo(0f, 0f)
            this.lineTo(200f, 0f)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            Log.e("===", "==")

            it.drawPath(mPath, mPaint)
        }
    }
    private var current:Float = 200f
    fun translateTo(percent: Float) {
        Log.e("==","$percent")
        mPath.reset()
        mPath.quadTo(200f, 0f, 200 *percent, 0f)
        current = 200 *(1+percent)
        mPath.moveTo(current,0f)
        postInvalidate()

    }

    fun translateBack(percent: Float) {

        mPath.quadTo(current, 0f, percent*200, 0f)

        postInvalidate()

    }


}