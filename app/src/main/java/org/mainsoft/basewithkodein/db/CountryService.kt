package org.mainsoft.basewithkodein.db

import io.objectbox.BoxStore
import org.mainsoft.basewithkodein.db.base.BaseService
import org.mainsoft.basewithkodein.net.response.CountryResponse

class CountryService(db: BoxStore) : BaseService<CountryResponse>(db) {

    override fun getType(): Class<CountryResponse> {
        return CountryResponse::class.java
    }

}