package org.base.test_shared.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.base.network.models.base.ResponseError

object DefaultRemoteConfig {

    private val moshi = Moshi.Builder().build()

    fun provideRemoteErrorAdapter(): JsonAdapter<ResponseError> =
        moshi.adapter(ResponseError::class.java)
}
