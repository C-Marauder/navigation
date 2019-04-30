package com.example.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.xqy.fragment.navigation.NavigatorCallback

class MainActivity : AppCompatActivity(), NavigatorCallback, FragmentCallback {

    //    override val mHostFragment: Fragment by lazy {
//        getInstance<MainFragment>()
//    }
    private val mLoanFragment: LoanFragment by lazy {
        LoanFragment()
    }

    override fun doThings() {
        //showFragment(mLoanFragment)
    }

    //
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNavigator(R.navigation.nav)

    }

    override fun onBackPressed() {
        popOnBackPressed {
            super.onBackPressed()

        }
    }
}
