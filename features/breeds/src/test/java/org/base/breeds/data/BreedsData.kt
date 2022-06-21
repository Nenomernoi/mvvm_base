package org.base.breeds.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.base.common.models.data.BreedResponse
import org.base.test_shared.file.FileReaderUtil
import java.lang.reflect.Type

object BreedsData {
    private val moshi = Moshi.Builder().build()
    private val breedsResponseGenericType: Type = Types.newParameterizedType(
        List::class.java,
        BreedResponse::class.java
    )
    private val remoteBreedsAdapter: JsonAdapter<List<BreedResponse>> =
        moshi.adapter(breedsResponseGenericType)

    fun provideRemoteBreedsFromAssets(page: Int): List<BreedResponse> {
        return remoteBreedsAdapter.fromJson(
            FileReaderUtil.kotlinReadFileWithNewLineFromResources(
                fileName = "breeds_$page.json"
            )
        ) ?: listOf()
    }
}
