package org.mainsoft.basewithkodein.screen.presenter

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import io.reactivex.functions.Consumer
import org.mainsoft.basewithkodein.db.CountryService
import org.mainsoft.basewithkodein.net.manager.GetCurrenciesManager
import org.mainsoft.basewithkodein.net.response.CountryResponse
import org.mainsoft.basewithkodein.screen.presenter.base.BaseListPresenter
import org.mainsoft.basewithkodein.screen.view.ExampleListView

class ExampleListPresenter(view: ExampleListView)
    : BaseListPresenter<CountryResponse>(view),
        GetCurrenciesManager.LoadListener {

    init {
        netManager = GetCurrenciesManager(api, this)
    }

    private val countryService: CountryService = CountryService(db)

    //////////////////////////////////////////////////////////////////////////////////////////////

    override fun onLoad(list: MutableList<CountryResponse>) {
        super.onLoad(list)
        if (list.isEmpty()) {
            return
        }
        countryService.reSave(list)

    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    override fun initData(context: Context, bundle: Bundle?, arguments: Bundle?) {
        if (bundle != null) {
            loadCache(bundle)
        }

        if (bundle == null && isDataEmpty()) {
            firstLoad()
        }
    }

    override fun firstLoad() {
        countryService.readAll(
                Consumer { t ->
                    if (t.isEmpty()) {
                        addSubscription((netManager as GetCurrenciesManager).getCityList("name;capital;currencies"))
                        return@Consumer
                    }
                    onLoad(t)
                })

    }

    /////////////////////////////////////////////////////////////////////////////////////////

    fun openItemScreen(position: Int) {
        val item = getItem(position)
        val bundle = bundleOf(ARGUMENT_ID to item.id, ARGUMENT_EXTRA_ID to item.name)
        getView<ExampleListView>()?.openItemScreen(bundle)
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    override fun loadCache(savedInstanceState: Bundle) {
        onLoad(savedInstanceState.getParcelableArrayList<CountryResponse>(BUNDLE_LIST_KEY))
    }

    override fun saveCache(outState: Bundle) {
        outState.putParcelableArrayList(BUNDLE_LIST_KEY, ArrayList(getData()))
    }

}