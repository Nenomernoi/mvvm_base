package org.base.network

import okhttp3.OkHttpClient

// This can be Internal. If we move the NetworkModule to here.
class HttpClientFactory {

    private val httpClient by lazy {

        OkHttpClient()
    }

    fun create(): OkHttpClient.Builder {
        return httpClient.newBuilder()
    }
}
