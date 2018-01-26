package org.mainsoft.basewithkodein.net

import io.reactivex.Observable
import org.mainsoft.basewithkodein.net.response.CountryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    ///////////////////////////////////////////// GET  //////////////////////////////////////////

    @GET("all")
    fun getCurrencies(@Query("fields")
                      fields: String): Observable<MutableList<CountryResponse>>  //name;capital;currencies

}