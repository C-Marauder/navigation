package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout


class ClipRevealFrame : FrameLayout {
    private var mRevealPath: Path? = null
    internal var mClipOutlines: Boolean = false
    internal var mCenterX: Float = 0.toFloat()
    internal var mCenterY: Float = 0.toFloat()
    internal var mRadius: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mRevealPath = Path()
        mClipOutlines = false
        setWillNotDraw(false)
    }

    fun setClipOutLines(shouldClip: Boolean) {
        mClipOutlines = shouldClip
    }

    fun setClipCenter(x: Int, y: Int) {
        mCenterX = x.toFloat()
        mCenterY = y.toFloat()
    }

    fun setClipRadius(radius: Float) {
        mRadius = radius
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if (!mClipOutlines) {
            super.onDraw(canvas)

            return
        }
        val state = canvas!!.save()
        mRevealPath!!.reset()
        mRevealPath!!.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW)
        canvas.clipPath(mRevealPath)
        super.draw(canvas)
        canvas.restoreToCount(state)
    }

}