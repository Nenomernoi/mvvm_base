package org.mainsoft.basewithkodein.db

import io.objectbox.BoxStore
import io.reactivex.functions.Consumer
import org.mainsoft.basewithkodein.db.base.BaseService
import org.mainsoft.basewithkodein.net.response.CountryResponse
       import org.mainsoft.basewithkodein.net.response.CountryResponse_

class CountryService(db: BoxStore) : BaseService<CountryResponse>(db) {

    override fun getType(): Class<CountryResponse> {
        return CountryResponse::class.java
    }

    fun readByName(search: String, consumer: Consumer<MutableList<CountryResponse>>) {
       addSubscribe(initDisposabe({ builder.equal(CountryResponse_.name, search).build().find() }, consumer))
    }

}