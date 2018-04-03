package org.mainsoft.basewithkodein.screen.presenter

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.animation.addListener
import androidx.content.edit
import androidx.content.systemService
import androidx.net.toUri
import androidx.os.bundleOf
import androidx.os.persistableBundleOf
import androidx.os.postAtTime
import androidx.os.postDelayed
import androidx.text.*
import androidx.time.component1
import androidx.time.component2
import androidx.time.component3
import androidx.time.component4
import androidx.view.*
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleView
import org.mainsoft.basewithkodein.util.Setting
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetTime


class ExamplePresenter(view: ExampleView) : BasePresenter(view) {

    private lateinit var image: File

    override fun initData(context: Context, bundle: Bundle?, arguments: Bundle?) {
        //
        //////////////////////////////////// URI //////////////////////////////////////////////////////////////
        /////KTX EXAMPLE

        // Kotlin:
        val uri = Uri.parse("http://ya.com")
        //  Kotlin with Android KTX:
        val uriKtx = "http://ya.com".toUri()

        /////////////////////////////////// SHARED PREFERENCES  //////////////////////////////////////////////////////////////

        val sharedPreferences = context.getSharedPreferences(Setting.Param.PREF_NAME, Activity.MODE_PRIVATE)
        // Kotlin:
        sharedPreferences.edit()
                .putBoolean("key", true)
                .apply()
        //  Kotlin with Android KTX:
        sharedPreferences.edit {
            putBoolean("key", true)
        }

        ///////////////////////////////////// TREE OBSERVER /////////////////////////////////////////////////////////////

        // Kotlin:
        val edtView = EditText(context)

        edtView.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        edtView.viewTreeObserver.removeOnPreDrawListener(this)
                        //DO SOMETHING
                        return true
                    }
                })
        //  Kotlin with Android KTX:
        edtView.doOnPreDraw {
            //DO SOMETHING
        }

        ///////////////////////////////// ANIMATOR /////////////////////////////////////////////////////////////////

        val animator = ValueAnimator.ofFloat(0f, 1f)

        // Kotlin:
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                //
            }

            override fun onAnimationEnd(animation: Animator?) {
                //
            }

            override fun onAnimationCancel(animation: Animator?) {
                //
            }

            override fun onAnimationStart(animation: Animator?) {
                //
            }

        })
        //  Kotlin with Android KTX: можно не все наследовать
        animator.addListener(
                onEnd = {},
                onStart = {},
                onCancel = {},
                onRepeat = {}
        )

        ///////////////////////////////// SYSTEM SERVICE /////////////////////////////////////////////////////////////////

        // Kotlin:
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //  Kotlin with Android KTX:
        val immKtx = context.systemService<InputMethodManager>()


        ///////////////// /////////////////////// DATE /////////////////////////////////////////////////

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val (seconds, nanoseconds) = Instant.now()
            val (year, month, day) = LocalDate.now()
            val (hour, minute, second, nanosecond) = LocalTime.now()
            val (localTime, ZoneOffset) = OffsetTime.now()
        }

        ///////////////// /////////////////////// Handler /////////////////////////////////////////////////

        // Kotlin:
        Handler().postAtTime(object : Runnable {
            override fun run() {
                //DO SOMETHING
            }
        }, 200L)

        Handler().postDelayed(object : Runnable {
            override fun run() {
                //DO SOMETHING
            }
        }, 200L)

        //  Kotlin with Android KTX:
        Handler().postAtTime(uptimeMillis = 200L) {
            //DO SOMETHING
        }
        Handler().postDelayed(delayInMillis = 200L) {
            //DO SOMETHING
        }

        ///////////////// /////////////////////// BUNDLE /////////////////////////////////////////////////

        // Kotlin:
        val bundle = Bundle()
        bundle.putInt("some_key", 12)
        bundle.putInt("another_key", 15)

        //  PersistableBundle  - bundle c типовыми полями

        //  Kotlin with Android KTX:
        val bundleKtx1 = bundleOf("some_key" to 12, "another_key" to 15)
        val bundleKtx2 = persistableBundleOf("some_key" to 12, "another_key" to 15)


        /////////////////////////////// Text /////////////////////////////

        //  Kotlin with Android KTX:

        val builderKtx = SpannableStringBuilder("some_key")
                .bold { append("hi there") }
        val builderKtx2 = SpannableStringBuilder("some_key")
                .bold { italic { underline { append("hi there") } } }
                .backgroundColor(color = R.color.black) {
                    // Действие в builder
                }

        edtView.hint = buildSpannedString { bold { append("hitherejoe") } }


        /////////////////////////////// View /////////////////////////////

        //  Kotlin with Android KTX:
        edtView.setPadding(16)
        edtView.updatePadding(left = 16, right = 16, top = 16, bottom = 16)
        edtView.updatePaddingRelative(
                start = 16, end = 16, top = 16, bottom = 16)

        val params = edtView.layoutParams as LinearLayout.LayoutParams
        params.setMargins(16)
        params.updateMargins(left = 16, right = 16, top = 16, bottom = 16)
        params.updateMarginsRelative(
                start = 16, end = 16, top = 16, bottom = 16)

        //конвертировать View в Bitmap
        val bitmap = edtView.toBitmap(config = Bitmap.Config.ARGB_8888)

        ///////////////////////////// ViewGroup/////////////////////////////////

        //проверка, содержит ли ViewGroup конкретную View:
        val viewGroup: ViewGroup? = null
        val doesContain = viewGroup!!.contains(edtView)

        viewGroup.forEach {
            //doSomethingWithChild(it) it - child View
        }
        viewGroup.forEachIndexed { index, view ->
            //   doSomethingWithChild(index, view)
        }

        viewGroup.isEmpty()
        viewGroup.isNotEmpty()
        viewGroup.size
        // Удаление view из данной viewgroup
        viewGroup -= edtView
        // Добавление view в данную viewgroup
        viewGroup += edtView

        //Подобное есть для меню

       //  https:://android.github.io/android-ktx/core-ktx/index.html

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    fun openCamera(fr: BaseFragment) {
        RxPaparazzo.single(fr)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.data() != null) {
                        image = response.data().file
                        getView<ExampleView>()?.initImage(image.absolutePath)
                    }
                })
    }

    fun openAlbum(fr: BaseFragment) {
        RxPaparazzo.single(fr)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.data() != null) {
                        image = response.data().file
                        getView<ExampleView>()?.initImage(image.absolutePath)
                    }
                })
    }

}