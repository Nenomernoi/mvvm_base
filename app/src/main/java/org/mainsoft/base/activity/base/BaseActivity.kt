package org.mainsoft.base.activity.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.mainsoft.base.R
import org.mainsoft.base.screen.fragment.base.BaseFragment

abstract class BaseActivity : AppCompatActivity(), ActivityCallback {

    protected var fm: FragmentManager? = null

    protected open fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAnim()
        setContentView(getLayout())
        fm = supportFragmentManager
        initData()
        initDrawer()
        startScreen(savedInstanceState)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    protected open fun initData() {
        //
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    protected open fun startAnim() {
        //
    }

    protected open fun stopAnim() {
        //
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    private fun startScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            restoreFragments()
            setDrawerEnabled(true)
            return
        }
        if (fm?.fragments?.isEmpty() != false) {
            startNewTask()
        }
    }

    private fun restoreFragments() {
        supportFragmentManager.fragments
                .asSequence()
                .filterNotNull()
                .filterIsInstance<BaseFragment>()
                .toList()
                .forEach { it.onRestoreFragment() }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    override fun openFragment(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle?) {
        openFragment(fragmentClass, addToBackStack, args, false)
    }

    override fun openRootFragment(fragmentClass: Class<out BaseFragment>, args: Bundle?) {
        fm ?: return
        (0 until fm!!.backStackEntryCount)
                .map { fm!!.getBackStackEntryAt(it).id }
                .forEach { fm!!.popBackStack(it, FragmentManager.POP_BACK_STACK_INCLUSIVE) }
        openFragment(fragmentClass, true, args, true)
        setDrawerEnabled(true)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private fun openFragment(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle?,
                             isReplace: Boolean) {
        openScreen(fragmentClass, addToBackStack, args, isReplace)
        closeDrawer()
    }

    private fun openScreen(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle?,
                           isReplace: Boolean) {

        hideSoftKeyboard()

        try {
            if (supportFragmentManager.backStackEntryCount > 0) {
                setDrawerEnabled(false)
            }
            val fragment = createFragment(fragmentClass, args)
            val fragmentName = fragment.javaClass.simpleName
            val transaction = fm?.beginTransaction()

            if (!isReplace) {
                addAnimToFragment(transaction)
            }

            if (isReplace) {
                transaction?.replace(R.id.container, fragment, fragmentName)
            } else {
                transaction?.add(R.id.container, fragment, fragmentName)
            }

            if (addToBackStack) {
                transaction?.addToBackStack(null)
            }
            transaction?.commit()

        } catch (e: Exception) {
            Log.e(javaClass.simpleName, Log.getStackTraceString(e), e)
        }

    }

    protected open fun addAnimToFragment(transaction: FragmentTransaction?) {
        transaction?.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_right_slow,
                R.anim.slide_out_left_fast, R.anim.slide_in_left)
    }

    private fun createFragment(fragmentClass: Class<out BaseFragment>, args: Bundle?): BaseFragment {
        val fragment = fragmentClass.newInstance()
        fragment.setHasOptionsMenu(true)
        fragment.arguments = args
        return fragment
    }

    ////////////////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() {
        hideSoftKeyboard()

        if (fm?.backStackEntryCount == 1) {
            finish()
            return
        }

        super.onBackPressed()

        val fr = fm?.findFragmentById(R.id.container) as? BaseFragment
        fr?.onResumeFromBackStack()
        setDrawerEnabled(fm?.backStackEntryCount == 1)
    }

    ////////////////////////////////////////////////////////////////////////////////////

    protected open fun setDrawerEnabled(isEnable: Boolean) {
        //
    }

    protected open fun closeDrawer() {
        //
    }

    protected open fun initDrawer() {
        //
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    override fun openNewActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun openResultActivity(intent: Intent, code: Int) {
        startActivityForResult(intent, code)
    }

    ////////////////////////////////////////////////////////////////////////////////////

    override fun hideSoftKeyboard() {
        Handler().post {
            var token: IBinder? = null
            if (currentFocus != null) {
                token = currentFocus?.windowToken
            }
            if (token != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(token, 0)
            }
        }
    }

    abstract fun startNewTask()
}
