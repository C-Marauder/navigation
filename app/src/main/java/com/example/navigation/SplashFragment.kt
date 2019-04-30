package com.example.navigation

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.xqy.fragment.navigation.NavigatorCallback


class SplashFragment : Fragment(){

    private lateinit var mFragmentCallback: NavigatorCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentCallback = context as NavigatorCallback
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return AppCompatTextView(context!!).apply {
            text = "SplashFragment"
            textSize = 24f
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.shadow)
            setOnClickListener {
                mFragmentCallback.navigate(R.id.loan_fragment)

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // mFragmentCallback.doThings()

    }
}