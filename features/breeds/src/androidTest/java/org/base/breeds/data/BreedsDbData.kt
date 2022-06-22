package org.base.breeds.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.common.models.domain.Breed
import org.base.test_shared.file.FileReaderUtil
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
object BreedsDbData {
    private val moshi = Moshi.Builder().build()
    private val breedsResponseGenericType: Type = Types.newParameterizedType(
        List::class.java,
        Breed::class.java
    )
    private val remoteBreedsAdapter: JsonAdapter<List<Breed>> =
        moshi.adapter(breedsResponseGenericType)

    fun provideRemoteBreedsFromAssets(): List<Breed> {
        return remoteBreedsAdapter.fromJson(
            FileReaderUtil.kotlinReadFileWithNewLineFromResources(
                fileName = "breeds.json"
            )
        ) ?: listOf()
    }
}
