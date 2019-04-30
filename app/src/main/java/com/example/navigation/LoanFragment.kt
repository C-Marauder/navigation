package com.example.navigation

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.xqy.fragment.navigation.NavigatorCallback
import kotlinx.android.synthetic.main.f_loan.*

class LoanFragment:Fragment() {
    private lateinit var callback: NavigatorCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as NavigatorCallback
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//         mViewPager = ViewPager(context!!).apply {
//
//            setOnClickListener {
//
//            }
//        }
//        return FrameLayout(context!!).apply {
//            addView(mViewPager)
//        }

        return inflater.inflate(R.layout.f_loan,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPager.adapter = MyAdapter()
        mViewPager.offscreenPageLimit  =2
        text.setOnClickListener {
            callback.navigate(R.id.login_fragment)
        }

    }
    private inner class MyAdapter: PagerAdapter() {
        private val items:MutableList<AppCompatTextView> by lazy {
            mutableListOf<AppCompatTextView>()
        }
        init {
            items.add(AppCompatTextView(context!!).apply {
                text ="A"
                textSize = 24f
                gravity = Gravity.CENTER
                setBackgroundResource(android.R.color.holo_green_light)
                setOnClickListener {

                }
            })
            items.add(AppCompatTextView(context!!).apply {
                text ="B"
                textSize = 24f
                gravity = Gravity.CENTER
                setBackgroundResource(android.R.color.holo_red_light)
                setOnClickListener {

                }
            })
        }
        override fun getCount(): Int =2

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(items[position])
            return items[position]
        }

    }
}