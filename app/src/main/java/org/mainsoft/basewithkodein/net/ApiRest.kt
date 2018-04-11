package org.mainsoft.basewithkodein.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiRest {

    companion object {

        private var BASE_URL = "https://restcountries.eu"
        private var BASE_CONTENT = "/rest/v2/"

        internal fun getApi(): Api {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val builder = Retrofit.Builder().baseUrl(BASE_URL + BASE_CONTENT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(
                            GsonConverterFactory.create())
                    .client(client)

            return builder.build().create(Api::class.java)
        }

    }
}