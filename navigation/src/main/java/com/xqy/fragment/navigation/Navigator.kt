package com.xqy.fragment.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.util.Xml
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.xmlpull.v1.XmlPullParser


@SuppressLint("ResourceType")
internal class Navigator private constructor(@NavigationRes private val graphId: Int) {
    companion object {
        @SuppressLint("ResourceType")
        @IdRes
        private const val CONTAINER_ID = 0x1
        private const val TAG: String = "Navigator"
    }

    private val mFragmentNames: SparseArray<String> by lazy {
        SparseArray<String>()
    }
    private lateinit var mFragment: Fragment
    private lateinit var mAppCompatActivity: AppCompatActivity
    private lateinit var mContext: Context
    lateinit var mContainer: FragmentContainerLayout
    private lateinit var mFragmentManager: FragmentManager
    private var mIsFirst: Boolean = true

    constructor(@NavigationRes graphId: Int, fragment: Fragment) : this(graphId) {
        this.mFragment = fragment
        this.mFragmentManager = mFragment.childFragmentManager
        this.mContext = mFragment.activity as AppCompatActivity
        mContainer = FragmentContainerLayout(mContext).apply {
            id = CONTAINER_ID
        }
        init()
    }

    constructor(@NavigationRes graphId: Int, activity: AppCompatActivity) : this(graphId) {
        this.mAppCompatActivity = activity
        this.mFragmentManager = mAppCompatActivity.supportFragmentManager
        this.mContext = mAppCompatActivity
        mContainer = FragmentContainerLayout(mContext).apply {
            id = CONTAINER_ID
        }
        val rootView = mAppCompatActivity.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(mContainer)
        init()
    }

    private fun init() {
        var startId: Int = -1
        val parser = mContainer.resources.getXml(graphId)
        val attributeSet = Xml.asAttributeSet(parser)
        try {
            var type = parser.next()
            while (type != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
                type = parser.next()
            }
            val a = mContainer.resources.obtainAttributes(attributeSet, R.styleable.NavGraphNavigator)
            startId = a.getResourceId(R.styleable.NavGraphNavigator_startDestination, 0)
            a.recycle()
            val innerDepth = parser.depth + 1
            type = parser.next()
            while (type != XmlPullParser.END_DOCUMENT && (parser.depth >= innerDepth || type != XmlPullParser.END_TAG)) {
                if (type != XmlPullParser.START_TAG) {
                    type = parser.next()
                    continue
                }
                val attr = mContext.resources.obtainAttributes(attributeSet, R.styleable.NavAction)
                val fragmentId = attr.getResourceId(R.styleable.NavAction_android_id, 0)
                attr.recycle()

                val attrName = mContext.resources.obtainAttributes(attributeSet, R.styleable.NavArgument)

                val name = attrName.getString(R.styleable.NavArgument_android_name)
                attrName.recycle()
                mFragmentNames.put(fragmentId, name)
                type = parser.next()

            }

        } catch (e: Exception) {

        } finally {
            parser.close()
        }

        mContainer.setSlidingCallback {
            popFragment()
        }
        val primaryFragment = createFragment(startId)
        mFragmentManager.beginTransaction()
            .add(mContainer.id, primaryFragment)
            .commitAllowingStateLoss()
    }

    private fun createFragment(navId: Int): Fragment {
        val pgName = mFragmentNames[navId] ?: throw Exception("there is no resource id $navId")
        return Class.forName(pgName).newInstance() as Fragment
    }

    //private
    fun navigate(@IdRes navId: Int, bundle: Bundle? = null) {
        val target = createFragment(navId)
        bundle?.let {
            target.arguments = bundle
        }
        mFragmentManager.beginTransaction()

            .apply {
                if (mIsFirst) {
                    replace(mContainer.id, target)
                } else {
                    setCustomAnimations(R.anim.slide_left_in, 0)
                        .add(mContainer.id, target)
                        .runOnCommit {
                            val translateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out)
                            val childView = mContainer.getChildAt(mContainer.childCount - 2)
                            childView.startAnimation(translateAnimation)

                        }


                }
                mIsFirst = false

            }.commitNowAllowingStateLoss()


    }

    private fun popFragment() {
        val lastFragment = mFragmentManager.fragments.last()
        mFragmentManager.beginTransaction()
            .remove(lastFragment)
            .commitNowAllowingStateLoss()
    }


    fun popOnBackPressed(onBackPressed: () -> Unit) {
        val fragmentCount = mFragmentManager.fragments.size
        if (fragmentCount == 1) {
            (mContext as AppCompatActivity).finish()
        } else {
            val lastFragment = mFragmentManager.fragments.last()
            mFragmentManager.beginTransaction()
                .setCustomAnimations(0, R.anim.slide_right_out)
                .remove(lastFragment)
                .runOnCommit {
                    val translateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in)
                    val childView = mContainer.getChildAt(mContainer.childCount - 1)
                    childView.startAnimation(translateAnimation)

                }
                .commitNowAllowingStateLoss()

        }
    }


}