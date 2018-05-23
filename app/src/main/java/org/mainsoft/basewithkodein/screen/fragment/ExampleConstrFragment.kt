package org.mainsoft.basewithkodein.screen.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.fragment_constr.*
import org.kodein.direct
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.ExampleConstrPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleConstView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ExampleConstrFragment : BaseFragment(), ExampleConstView {

    override fun getLayout() = R.layout.fragment_constr

    init {
        presenter = App.kodein.direct.instance<ExampleConstView, ExampleConstrPresenter>(arg = this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): ExampleConstrFragment {
            val pageFragment = ExampleConstrFragment()
            val arguments = Bundle()
            arguments.putLong(BasePresenter.ARGUMENT_PAGE_NUMBER, position.toLong())
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    override fun initData() {
        initWidthHeight()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun initListeners() {
        button?.setOnClickListener {
            val set = ConstraintSet()
            set.clone(cnstMain)
            changeConstraints(set)
            TransitionManager.beginDelayedTransition(cnstMain)
            set.applyTo(cnstMain)
        }
        button2?.setOnClickListener {
            val set = ConstraintSet()
            set.clone(cnstMain)
            changeDefConstraints(set)
            TransitionManager.beginDelayedTransition(cnstMain)
            set.applyTo(cnstMain)

            createPdf()
        }
    }

    private fun changeConstraints(set: ConstraintSet) {

        set.connect(R.id.textView, ConstraintSet.LEFT, R.id.textView2, ConstraintSet.LEFT)
        set.connect(R.id.textView, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 48)

        set.constrainCircle(R.id.button2, R.id.button, 500, 280f)

    }

    private fun changeDefConstraints(set: ConstraintSet) {

        set.connect(R.id.textView, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(R.id.textView, ConstraintSet.TOP, R.id.imageView, ConstraintSet.TOP, 48)

        set.constrainCircle(R.id.button2, R.id.button, 300, 311f)

    }

    private fun createPdf() {

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(widthRoot, heightRoot, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        canvas.drawPaint(paint)
        var bitmap = Bitmap.createScaledBitmap(
                loadBitmapFromView(cnstMain, cnstMain.width, cnstMain.height),
                widthRoot, heightRoot, true)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/RxPaparazzo")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val targetPdf = dir.absolutePath + "/test.pdf"
        val filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))
        } catch (e: IOException) {
            Log.e("Save pdf", e.message)
        }
        document.close()

    }

    private fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        v.draw(Canvas(b))
        return b
    }

}
