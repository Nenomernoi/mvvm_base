package org.mainsoft.basewithkodein.screen.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_base.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.adapter.GridAdapter
import org.mainsoft.basewithkodein.adapter.base.SpannedGridLayoutManager
import org.mainsoft.basewithkodein.listener.EndlessScrollListener
import org.mainsoft.basewithkodein.screen.fragment.base.BaseEndlessMainListFragment
import org.mainsoft.basewithkodein.screen.presenter.ExampleGridPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleGridView

class ExampleGridFragment : BaseEndlessMainListFragment<String>(), ExampleGridView {

    init {
        presenter = App.kodein.direct.instance<ExampleGridView, ExampleGridPresenter>(arg = this)
    }

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): ExampleGridFragment {
            val pageFragment = ExampleGridFragment()
            val arguments = Bundle()
            arguments.putLong(BasePresenter.ARGUMENT_PAGE_NUMBER, position.toLong())
            pageFragment.arguments = arguments
            return pageFragment
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////

    override fun getLayout(): Int = R.layout.fragment_list_base

    //////////////////////////////////////////////////////////////////////////////////////

    override fun initAdapter() {
        initEndless()
        adapter = GridAdapter(getData())
        rvMain?.addOnScrollListener(endLess ?: return)
        rvMain?.setHasFixedSize(true)
    }

    override fun loadNext() {
        getPresenter<ExampleGridPresenter>()?.firstLoad()
    }

    override fun initRclView() {
        val manager = SpannedGridLayoutManager(activity,
                SpannedGridLayoutManager.GridSpanLookup { position ->
                    when (position % 18) {
                        0, 10 -> SpannedGridLayoutManager.SpanInfo(2, 2)
                        else -> SpannedGridLayoutManager.SpanInfo(1, 1)
                    }
                },
                3,  // number of columns
                1f // default size of item
        )
        rvMain?.layoutManager = manager
    }

    override fun openItemScreen(bundle: Bundle) {
        //
    }
}