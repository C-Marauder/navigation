package com.example.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.xqy.fragment.navigation.NavigatorCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainFragment:Fragment() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
            R.id.navigation_home -> {
                mFragmentCallback.doThings()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //go(loanFragment)
//                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private lateinit var mFragmentCallback: FragmentCallback
    private lateinit var mFragmentManagerCallback: NavigatorCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentCallback = context as FragmentCallback
        this.mFragmentManagerCallback = context as NavigatorCallback
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.activity_main,container,false).apply {


//            addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//                val cx = v.width / 2
//                val cy = v.height / 2
//                val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
//                val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius)
//                anim.duration = 500
//                anim.interpolator = AccelerateDecelerateInterpolator()
//                anim.start()
//
//            }
        }


        return mView
    }
         //val loanFragment = getInstance<LoanFragment>()

//    private val homeFragment by lazy {
//        getInstance<HomeFragment>()
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //go(homeFragment)
    }

    private fun go(fragment: Fragment){


    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("==","onDestroyView")
    }
}