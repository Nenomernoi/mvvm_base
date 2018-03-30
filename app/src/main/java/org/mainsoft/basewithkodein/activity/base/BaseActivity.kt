package org.mainsoft.basewithkodein.activity.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.location.*
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.util.DialogUtil
import org.mainsoft.basewithkodein.util.Setting

abstract class BaseActivity : AppCompatActivity(), ActivityCallback {

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        private const val MAX_DISTANCE: Double = 10.0
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        const val UPDATE_SCREEN = "screenUpdate"
    }

    protected val setting: Setting by App.kodein.instance()

    private var fm: FragmentManager? = null

    private var client: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationSettingsRequest: LocationSettingsRequest

    protected var latitude: Double = BasePresenter.EMPTY
    protected var longitude: Double = BasePresenter.EMPTY

    private var isStartSetting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        fm = supportFragmentManager
        initData()
        initDrawer()
        startScreen(savedInstanceState)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    protected open fun getLayout() = R.layout.activity_main

    ///////////////////////////////////////////////////////////////////////////////////////////

    protected open fun initData() {
        initLocation()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    private fun startScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            restoreFragments()
            return
        }
        if (fm!!.fragments!!.size == 0) {
            startNewTask()
        }
    }

    private fun restoreFragments() {

        supportFragmentManager.fragments
                .filterNotNull()
                .filterIsInstance<BaseFragment>()
                .forEach { it.onRestoreFragment() }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (fm?.fragments?.size == 0 || intent!!.extras.getBoolean(UPDATE_SCREEN, false)) {
            startNewTask()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    override fun openNewActivity(intent: Intent) {
        intent.putExtra(UPDATE_SCREEN, true)
        startActivity(intent)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    override fun openFragment(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle) {
        openFragment(fragmentClass, addToBackStack, args, false)
    }

    override fun openRootFragment(fragmentClass: Class<out BaseFragment>, args: Bundle) {

        (0 until fm!!.backStackEntryCount)
                .map { fm!!.getBackStackEntryAt(it).id }
                .forEach { fm!!.popBackStack(it, FragmentManager.POP_BACK_STACK_INCLUSIVE) }
        openFragment(fragmentClass, true, args, true)
        setDrawerEnabled(true)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private fun openFragment(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle,
                             isReplace: Boolean) {
        openScreen(fragmentClass, addToBackStack, args, isReplace)
        closeDrawer()
    }

    private fun openScreen(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle,
                           isReplace: Boolean) {

        hideSoftKeyboard()

        try {
            if (supportFragmentManager.backStackEntryCount > 0) {
                setDrawerEnabled(false)
            }
            val fragment = createFragment(fragmentClass, args)
            val fragmentName = fragment.javaClass.simpleName
            val transaction = fm!!.beginTransaction()

            if (isReplace) {
                transaction.replace(R.id.container, fragment, fragmentName)
            } else {
                transaction.add(R.id.container, fragment, fragmentName)
            }

            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()

        } catch (e: Exception) {
            Log.e(javaClass.simpleName, Log.getStackTraceString(e), e)
        }

    }

    private fun openChildScreen(fragmentClass: Class<out BaseFragment>, addToBackStack: Boolean, args: Bundle,
                                isReplace: Boolean) {
        hideSoftKeyboard()
        try {
            val fragment = createFragment(fragmentClass, args)
            val fragmentName = fragment.javaClass.simpleName
            val transaction = fm!!.beginTransaction()

            if (isReplace) {
                transaction.replace(R.id.container, fragment, fragmentName)
            } else {
                transaction.add(R.id.container, fragment, fragmentName)
            }

            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()

        } catch (e: Exception) {
            Log.e(javaClass.simpleName, Log.getStackTraceString(e), e)
        }
    }

    private fun createFragment(fragmentClass: Class<out BaseFragment>, args: Bundle): BaseFragment {
        val fragment = fragmentClass.newInstance()
        fragment.setHasOptionsMenu(true)
        fragment.arguments = args
        return fragment
    }

    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    override fun initLocation() {
        if (checkPermissions()) {
            return
        }

        client = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(l: LocationResult?) {
                val distance = getDistanceCorrect(l!!)
                if (distance >= MAX_DISTANCE) {
                    this@BaseActivity.latitude = l.lastLocation.latitude
                    this@BaseActivity.longitude = l.lastLocation.longitude

                    readyLocation(distance)
                }
            }
        }
        settingsClient = LocationServices.getSettingsClient(this)

        locationRequest = LocationRequest()
        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()

        settingsClient.checkLocationSettings(locationSettingsRequest).addOnCompleteListener(this) {
            client?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }.addOnFailureListener({ _ -> errorLocation() })
    }

    private fun getDistanceCorrect(l: LocationResult): Double {
        if (latitude == BasePresenter.EMPTY && longitude == BasePresenter.EMPTY) {
            return MAX_DISTANCE
        }
        val locationA = Location("A")
        locationA.latitude = l.lastLocation.latitude
        locationA.longitude = l.lastLocation.longitude
        val locationB = Location("B")
        locationB.latitude = latitude
        locationB.longitude = longitude
        return locationA.distanceTo(locationB).toDouble()
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    override fun getLocation() {
        if (latitude != BasePresenter.EMPTY && longitude != BasePresenter.EMPTY) {
            readyLocation(0.0)
            return
        }

        if (client == null) {
            initLocation()
        }
    }

    private fun readyLocation(distance: Double) {
        supportFragmentManager
                .fragments
                .filterNotNull()
                .filterIsInstance<BaseFragment>()
                .forEach {
                    it.readyLocation(latitude, longitude, distance)
                }
    }

    private fun errorLocation() {
        supportFragmentManager
                .fragments
                .filterNotNull()
                .filterIsInstance<BaseFragment>()
                .forEach { it.errorLocation() }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    override fun hideSoftKeyboard() {
        Handler().post {
            var token: IBinder? = null
            if (currentFocus != null) {
                token = currentFocus!!.windowToken
            }
            if (token != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(token, 0)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() {
        hideSoftKeyboard()

        if (fm!!.backStackEntryCount == 1) {
            finish()
            return
        }

        super.onBackPressed()

        val fr = fm!!.findFragmentById(R.id.container) as BaseFragment
        fr.onResumeFromBackStack()
        setDrawerEnabled(fm!!.backStackEntryCount == 1)
    }

    ////////////////////////////////////////////////////////////////////////////////////

    override fun openPermission(listener: PermissionListener, vararg permissions: String) {
        openPermissionBase(false, listener, *permissions)
    }

    override fun openPermissionBase(isRepeat: Boolean, listener: PermissionListener, vararg permissions: String) {
        RxPermissions(this).requestEach(*permissions).subscribe { permission ->
            if (permission.granted) {
                listener.onComplete()
            } else if (permission.shouldShowRequestPermissionRationale) {

                if (!isRepeat) {
                    listener.onError()
                    return@subscribe
                }

                openPermission(listener, *permissions)

            } else {

                if (!isStartSetting) {

                    isStartSetting = true

                    listener.onError()

                    DialogUtil.Companion.showErrorPermissionDialog(this,
                            DialogInterface.OnClickListener { dialog, _ ->
                                startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + packageName)))
                                dialog.dismiss()
                                isStartSetting = false
                            }, DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                        listener.onError()
                        isStartSetting = false
                    })
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment?.onActivityResult(requestCode, resultCode, data)
        }
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

    ////////////////////////////////////////////////////////////////////////////////////

    abstract fun startNewTask()

}