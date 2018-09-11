package org.mainsoft.basewithkodein.net.manager

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.mainsoft.basewithkodein.net.Api
import org.mainsoft.basewithkodein.net.manager.base.BaseListener
import org.mainsoft.basewithkodein.net.manager.base.BaseNetManager
import org.mainsoft.basewithkodein.net.response.CountryResponse

class GetCurrenciesManager(api: Api, listener: LoadListener) : BaseNetManager(api, listener) {

    fun getCityList(field: String): Disposable {

        listener?.showHideProgress(true)

        return api.getCurrencies(field)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = { results ->
                            (listener as? LoadListener)?.onLoad(results)
                        },
                        onError = consumerError,
                        onComplete = complete
                )
    }

    interface LoadListener : BaseListener {
        fun onLoad(list: MutableList<CountryResponse>)
    }
}