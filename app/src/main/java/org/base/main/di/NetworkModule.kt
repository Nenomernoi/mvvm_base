package org.base.main.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.base.main.BuildConfig
import org.base.network.HttpClientFactory
import org.base.network.models.base.ResponseError
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = DI.Module(name = "NetworkModule") {
    bind<HttpClientFactory>() with singleton { HttpClientFactory() }
    bind<OkHttpClient.Builder>() with singleton { provideOkHttpBuilder(instance()) }
    bind<OkHttpClient>() with singleton { provideClient(instance()) }
    bind<Retrofit>() with singleton { provideRetrofitBuilder(instance()) }
    bind<Moshi>() with singleton { provideMoshi() }
    bind<JsonAdapter<ResponseError>>() with singleton { provideJsonErrorAdapter(instance()) }
}

internal fun provideOkHttpBuilder(
    httpClientFactory: HttpClientFactory
): OkHttpClient.Builder {
    return httpClientFactory.create()
}

internal fun provideClient(
    clientBuilder: OkHttpClient.Builder
): OkHttpClient {
    clientBuilder
        .addInterceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader("Content-Type", "application/json")
                addHeader("x-api-key", BuildConfig.API_KEY)
                url(chain.request().url)
            }
            return@addInterceptor chain.proceed(request.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
    }
    return clientBuilder.build()
}

fun provideRetrofitBuilder(
    okHttpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

internal fun provideMoshi(): Moshi {
    return Moshi.Builder().build()
}

fun provideJsonErrorAdapter(moshi: Moshi): JsonAdapter<ResponseError> {
    return moshi.adapter(ResponseError::class.java)
}
