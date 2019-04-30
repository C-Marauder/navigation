package com.xqy.fragment.navigation
import android.animation.FloatEvaluator
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.NestedScrollingParentHelper
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager


class FragmentContainerLayout:FrameLayout {

    companion object {
        private const val INVALID_POINTER = -1

    }
    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ){
        init()
    }
    private val parentHelper: NestedScrollingParentHelper by lazy {
        NestedScrollingParentHelper(this)
    }
    private lateinit var mViewDragHelper: ViewDragHelper
    private  var originalX:Float = 0f
    private  var originalY:Float = 0f
    private var mActivePointerId:Int =-1
    private var mSlidingBack:()->Unit={}
    private var mTouchSlop:Int = 0

    private fun init(){
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

        this.mViewDragHelper = ViewDragHelper.create(this,1f,ViewDragCallback())
        setBackgroundResource(android.R.color.black)
    }

    private fun canScroll(childView:View):Boolean{
        if (childView is ViewGroup){
            if (childView is ViewPager){
                return childView.currentItem != 0
            }
            val count = childView.childCount
            if (count>=1){
                for (i in 0 until count){
                    return canScroll(childView.getChildAt(i))
                }
            }
        }

        return false
    }
    fun setSlidingCallback(callback:()->Unit){
        this.mSlidingBack = callback
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (canScroll(getChildAt(childCount-1))){
            return false
        }
        val event = ev!!.actionMasked
        val childCount = this.childCount
        return  if (childCount<=1){
            return when(event){
                MotionEvent.ACTION_DOWN->{
                    mActivePointerId = ev.getPointerId(0)
                    if (mActivePointerId == INVALID_POINTER){
                        return false
                    }
                    originalY= ev.getY(mActivePointerId)
                    originalX = ev.getX(mActivePointerId)
                    return false
                }
                MotionEvent.ACTION_MOVE->{
                    val moveX = ev.getX(mActivePointerId) - originalX
                    val moveY  = ev.getY(mActivePointerId) - originalY
                    return moveX > mTouchSlop || Math.abs( moveY)>mTouchSlop
                }
                else -> false
            }

        }else{

            when(event){
                MotionEvent.ACTION_DOWN->{
                    mActivePointerId = ev.getPointerId(0)
                    if (mActivePointerId == INVALID_POINTER){
                        return false
                    }
                    originalY= ev.getY(mActivePointerId)
                    originalX = ev.getX(mActivePointerId)

                }
                MotionEvent.ACTION_MOVE->{
                   val endY  = ev.getY(mActivePointerId)
                    if (endY> originalY){
                        return false
                    }
                    val moveX = ev.getX(mActivePointerId) - originalX
                    val moveY = endY - originalY

                   val angle = Math.abs(Math.atan((moveY/moveX).toDouble())/(Math.PI/180))
                    if (angle>15 || moveX<mTouchSlop){
                        return false
                    }



                }

            }
            mViewDragHelper.shouldInterceptTouchEvent(ev)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val childCount = this.childCount
        if (childCount<=1){
            performClick()
            return super.onTouchEvent(event)
        }
        mViewDragHelper.processTouchEvent(event!!)
        return true
    }

    override fun computeScroll() {
        if (mViewDragHelper.continueSettling(true)){
            invalidate()
        }
    }
    private inner class ViewDragCallback : ViewDragHelper.Callback() {
        private var mPreView:View?=null
        private val mRange:Int by lazy {
            Resources.getSystem().displayMetrics.widthPixels
        }
        private val mFloatEvaluator:FloatEvaluator by lazy {
            FloatEvaluator()
        }

        private var moveX:Int = 0
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            val childCount = this@FragmentContainerLayout.childCount
           val isLastChild  =child == this@FragmentContainerLayout.getChildAt(childCount-1)
            if (isLastChild && mPreView == null){
                mPreView = this@FragmentContainerLayout.getChildAt(childCount-2)

            }
            return isLastChild
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            moveX =if (left<0){
                0
            }else{
                left
            }
            return moveX
        }
        override fun getViewHorizontalDragRange(child: View): Int {
            return mRange
        }
        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            val mPercent = left*1f/mRange
            if (mPercent == 1f){
                this@FragmentContainerLayout.mSlidingBack()
                mPreView!!.translationX = 0f
                mPreView = null
            }else if(mPercent == 0f){
                mPreView!!.translationX = 0f
            }else{
                mPreView!!.translationX =mFloatEvaluator.evaluate(mPercent,-mRange*2/5,0)
                //mPreView!!.scaleY =mFloatEvaluator.evaluate(mPercent,0.98,1f)
            }
        }
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
           val finalPosition = if (releasedChild.left>mRange/2){
               mRange
            }else{
               0
            }
            mViewDragHelper.settleCapturedViewAt(finalPosition,0)
            invalidate()
        }

    }


}