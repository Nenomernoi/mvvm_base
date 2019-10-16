package org.mainsoft.basewithkodein.screen.fragment.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_base.*
import org.kodein.di.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.activity.base.ActivityCallback
import org.mainsoft.basewithkodein.listener.BaseEditTextListener
import org.mainsoft.basewithkodein.listener.DrawableClickListener
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.base.BaseView
import org.mainsoft.basewithkodein.util.DialogUtil
import org.mainsoft.basewithkodein.util.PresenterUtil

abstract class BaseFragment : Fragment(), BaseView {

    private val presenterUtil: PresenterUtil by App.kodein.instance()

    protected open lateinit var activityCallback: ActivityCallback
    protected open lateinit var presenter: BasePresenter

    protected var heightRoot: Int = 0
    protected var widthRoot: Int = 0
    protected var heightRealRoot: Int = 0

    //////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListeners()
        onRestore(savedInstanceState, arguments)
    }

    protected open fun initData() {
        //
    }

    open fun onSearch(search: String) {
        //
    }

    protected open fun onRestore(savedInstanceState: Bundle?, arguments: Bundle?) {
        presenter.initData(activity!!, savedInstanceState, arguments)
    }

    protected open fun initListeners() {
        //
    }

    open fun updateScreen() {
        //
    }

    override fun firstLoad() {
        //
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    protected fun initWidthHeight() {
        val size = Point()
        activity?.windowManager?.defaultDisplay?.getSize(size)
        heightRoot = size.y
        widthRoot = size.x
        activity?.windowManager?.defaultDisplay?.getRealSize(size)
        heightRealRoot = size.y
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun readyLocation(lat: Double, lng: Double, distance: Double) {
        presenter.readyLocation(lat, lng, distance)
    }

    fun errorLocation() {
        presenter.errorLocation()
    }

    override fun getLocation() {
        activityCallback.getLocation()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun showToastError(message: Any?) {
        val mes: String = message as? String ?: getString(message as Int)
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }

    override fun showMessageError(message: Any?, listener: DialogInterface.OnClickListener?) {
        DialogUtil.showWarningErrorDialog(activity!!,
                message as? String ?: getString(message as Int),
                listener
                        ?: DialogInterface.OnClickListener { _: DialogInterface, _: Int -> showHideProgress(false) })
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    protected open fun initEditText(edt: EditText) {
        edt.addTextChangedListener(object : BaseEditTextListener {
            override fun afterTextChanged(s: Editable?) {
                afterTxt(0, s, edt)
                afterText()
            }
        })
    }

    protected fun afterTxt(res: Int, s: CharSequence?, edt: EditText) {

        if (s == null || s.toString().isEmpty()) {
            edt.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0)
            edt.setOnTouchListener(null)
            return
        }

        edt.setCompoundDrawablesWithIntrinsicBounds(res, 0, R.drawable.remove_text, 0)
        edt?.setOnTouchListener(object : DrawableClickListener.RightDrawableClickListener(edt) {
            override fun onDrawableClick(): Boolean {
                edt.setText("")
                return true
            }
        })
    }

    protected open fun afterText() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////

    open fun onRestoreFragment() {
        //
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    open fun onResumeFromBackStack() {
        //
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    protected fun openActivity(fragmentClass: Class<out Activity>, args: Bundle?) {
        val intent = Intent(activity, fragmentClass)
        intent.putExtras(args ?: Bundle())
        activityCallback.openNewActivity(intent)
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    protected fun openScreen(fragmentClass: Class<out BaseFragment>, args: Bundle) {
        presenterUtil.removePresenter(fragmentClass)
        activityCallback.openFragment(fragmentClass, true, args)
    }

    protected fun openBaseScreen(fragmentClass: Class<out BaseFragment>, args: Bundle) {
        presenterUtil.removePresenter(fragmentClass)
        activityCallback.openRootFragment(fragmentClass, args)
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        try {
            activityCallback = activity as ActivityCallback
        } catch (e: ClassCastException) {
            Log.e(javaClass.simpleName, e.message, e)
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun showError(error: String) {
        showHideProgress(false)
        Log.e(javaClass.simpleName, error)
    }

    override fun showHideProgress(isShowHide: Boolean) {
        pbLoad?.visibility = if (isShowHide) View.VISIBLE else View.GONE
    }

    fun showSoftKeyboard(view: View) {
        if (activity != null) {
            Handler().post {
                view.requestFocus()
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    fun hideSoftKeyboard(view: View?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var token: IBinder? = null
        if (view != null) {
            token = view.windowToken
        }
        if (token == null && activity?.currentFocus != null) {
            token = activity?.currentFocus?.windowToken
        }
        if (token != null) {
            imm.hideSoftInputFromWindow(token, 0)
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    abstract fun getLayout(): Int
    fun <V : Any> getPresenter(): V? = presenter as? V
}