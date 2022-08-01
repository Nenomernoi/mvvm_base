package org.base.di.modules

import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import org.base.di.BuildConfig
import org.base.network.HttpClientFactory
import org.base.network.models.base.ResponseError
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

val networkModule = DI.Module(name = "NetworkModule") {
    bind<HttpClientFactory>() with singleton { HttpClientFactory() }
    bind<OkHttpClient.Builder>() with singleton { provideOkHttpBuilder(instance()) }
    bind<TokenAuthenticator>() with singleton { TokenAuthenticator() }
    bind<OkHttpClient>() with singleton { provideClient(instance(), instance()) }
    bind<Retrofit>() with singleton { provideRetrofitBuilder(instance()) }
    bind<Moshi>() with singleton { provideMoshi() }
    bind<JsonAdapter<ResponseError>>() with singleton { provideJsonErrorAdapter(instance()) }
}

internal fun provideOkHttpBuilder(
    httpClientFactory: HttpClientFactory,
): OkHttpClient.Builder {
    return httpClientFactory.create()
}

internal fun provideClient(
    clientBuilder: OkHttpClient.Builder,
    tokenAuthenticator: TokenAuthenticator
): OkHttpClient {
    clientBuilder
        .authenticator(tokenAuthenticator)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader("Content-Type", "application/json")
                //addHeader("x-api-key", BuildConfig.API_KEY)
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

class TokenAuthenticator() : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        return runBlocking {
            val result = getUpdatedToken()
            val builder = response.request.newBuilder()
            if (result.version.isNotEmpty()) {
                builder.addHeader("x-api-key", BuildConfig.API_KEY)
            }
            builder.build()
        }
    }

    private suspend fun getUpdatedToken(): VersionResponse {

        val clientBuilder = OkHttpClient().newBuilder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
        }
        val okHttpClient = clientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(RefreshTokenApi::class.java).getVersion()
    }


    private interface RefreshTokenApi {
        @GET
        suspend fun getVersion(
            @Url url: String = BuildConfig.BASE_URL
        ): VersionResponse
    }

    data class VersionResponse(
        @field:Json(name = "version") val version: String,
        @field:Json(name = "message") val message: String,
    )

}
