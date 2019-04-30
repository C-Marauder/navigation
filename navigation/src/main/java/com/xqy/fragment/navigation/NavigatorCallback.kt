package com.xqy.fragment.navigation

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface NavigatorCallback {

    fun createNavigator(@NavigationRes graphIdRes: Int) {
        val navigator = if (this is AppCompatActivity) {
            Navigator(graphIdRes, this)
        }else{
            throw Exception("this subType of NavigatorCallback is not AppCompatActivity")
        }
        NavigatorManager.mInstance.mNavigators[this::class.java.simpleName] = navigator
    }

    fun createFragmentNavigator(@NavigationRes graphIdRes: Int):View{
        val navigator = if (this is Fragment) {
            Navigator(graphIdRes, this)
        }else{
            throw Exception("this subType of NavigatorCallback is not Fragment")
        }
        NavigatorManager.mInstance.mNavigators[this::class.java.simpleName] = navigator
        return navigator.mContainer
    }
    fun navigate(@IdRes navResId: Int) {
        val navigator = NavigatorManager.mInstance.mNavigators[this::class.java.simpleName]
        navigator?.navigate(navResId)
    }
    fun popOnBackPressed(onBackPressed: () -> Unit){
        val navigator = NavigatorManager.mInstance.mNavigators[this::class.java.simpleName]
        navigator?.popOnBackPressed{
            onBackPressed()
        }
    }
}