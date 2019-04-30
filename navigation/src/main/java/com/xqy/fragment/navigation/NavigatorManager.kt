package com.xqy.fragment.navigation

internal class NavigatorManager private constructor(){
    internal val mNavigators:HashMap<String,Navigator> by lazy {
        HashMap<String,Navigator>()
    }
    companion object {
        val mInstance:NavigatorManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            NavigatorManager()
        }
    }

}
