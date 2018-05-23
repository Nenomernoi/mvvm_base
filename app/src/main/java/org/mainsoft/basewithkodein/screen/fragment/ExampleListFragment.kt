package org.mainsoft.basewithkodein.screen.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.fragment_constr.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.kodein.direct
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.ExampleAdapter
import org.mainsoft.basewithkodein.base.OnItemClickListener
import org.mainsoft.basewithkodein.net.response.CountryResponse
import org.mainsoft.basewithkodein.screen.fragment.base.BaseMainListFragment
import org.mainsoft.basewithkodein.screen.presenter.ExampleListPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleListView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExampleListFragment : BaseMainListFragment<CountryResponse>(), ExampleListView {

    init {
        presenter = App.kodein.direct.instance<ExampleListView, ExampleListPresenter>(arg = this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): ExampleListFragment {

            val pageFragment = ExampleListFragment()
            val arguments = Bundle()
            arguments.putLong(BasePresenter.ARGUMENT_PAGE_NUMBER, position.toLong())
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun getLayout(): Int {
        return R.layout.fragment_list
    }

    override fun initData() {
        super.initData()
        initWidthHeight()
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun initAdapter() {
        adapter = ExampleAdapter(getPresenter<ExampleListPresenter>()!!.getData(), object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                createPdf()
                getPresenter<ExampleListPresenter>()?.openItemScreen(position)
            }
        })
    }

    override fun openItemScreen(bundle: Bundle) {
        openScreen(ExampleListFragment::class.java, bundle)
    }

    override fun getNewData() {
        getPresenter<ExampleListPresenter>()?.clearData()
        getPresenter<ExampleListPresenter>()?.firstLoad()
    }

    ////////////////////////////////////////////////////

    private fun createPdf() {

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(widthRoot, heightRoot, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        canvas.drawPaint(paint)
        var bitmap = Bitmap.createScaledBitmap(
                loadBitmapFromView(rvMain, rvMain.width, rvMain.height),
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
