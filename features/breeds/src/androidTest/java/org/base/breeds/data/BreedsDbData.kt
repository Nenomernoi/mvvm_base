package org.base.breeds.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.base.common.models.domain.Breed
import org.base.common.models.presentation.BreedUi
import org.base.test_shared.file.FileReaderUtil
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
object BreedsDbData {
    private val moshi = Moshi.Builder().build()
    private val breedsDomainResponseGenericType: Type = Types.newParameterizedType(
        List::class.java,
        Breed::class.java
    )
    private val domainBreedsAdapter: JsonAdapter<List<Breed>> =
        moshi.adapter(breedsDomainResponseGenericType)

    fun provideDomainBreedsFromAssets(): List<Breed> {
        return domainBreedsAdapter.fromJson(
            FileReaderUtil.kotlinReadFileWithNewLineFromResources(
                fileName = "breeds.json"
            )
        ) ?: listOf()
    }

    private val breedsUiResponseGenericType: Type = Types.newParameterizedType(
        List::class.java,
        BreedUi::class.java
    )
    private val uiBreedsAdapter: JsonAdapter<List<BreedUi>> =
        moshi.adapter(breedsUiResponseGenericType)

    fun provideUiBreedsFromAssets(): List<BreedUi> {
        return uiBreedsAdapter.fromJson(
            FileReaderUtil.kotlinReadFileWithNewLineFromResources(
                fileName = "breedsUI.json"
            )
        ) ?: listOf()
    }
}
