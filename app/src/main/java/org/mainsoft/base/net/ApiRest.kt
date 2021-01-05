package org.mainsoft.base.net

import org.mainsoft.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiRest {

    fun getApi(): Api {

        val client = (if (BuildConfig.ALLOW_INVALID_CERTIFICATE) getSafeOkHttpClient() else getUnsafeOkHttpClient())
                .addInterceptor(makeLoggingInterceptor())
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .connectTimeout(25, TimeUnit.SECONDS)
                .build()


        val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)

        return builder.build().create(Api::class.java)
    }

    ////////////////////////////////////////////////////////////////////////////////

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    ////////////////////////////////////////////////////////////////////////////////

    private fun getSafeOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    ////////////////////////////////////////////////////////////////////////////////

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    //
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    //
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            return builder
        } catch (e: Exception) {
            return OkHttpClient.Builder()
        }
    }

}
