package by.nrstudio.mvvm.net

import by.nrstudio.mvvm.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiRest {

	const val TIME_OUT = 25_000L
	const val IMAGE_TIME_OUT = 45_000
	const val FADE_TIME_OUT = 500

	private val client = (if (BuildConfig.ALLOW_INVALID_CERTIFICATE) getSafeOkHttpClient() else getUnsafeOkHttpClient())
		.addInterceptor(makeLoggingInterceptor())
		.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
		.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
		.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
		.build()

	fun getApi(): Api {

		val builder = Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create())
			.client(client)

		return builder.build().create(Api::class.java)
	}

	// //////////////////////////////////////////////////////////////////////////////

	private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
		val logging = HttpLoggingInterceptor()
		logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
		return logging
	}

	// //////////////////////////////////////////////////////////////////////////////

	private fun getSafeOkHttpClient(): OkHttpClient.Builder {
		return OkHttpClient.Builder()
	}

 	// //////////////////////////////////////////////////////////////////////////////

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

abstract class SafeApiRequest {
	suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

		val response = call.invoke()
		val message = StringBuilder()

		if (response.isSuccessful) {
			return response.body()!!
		} else {
			response.errorBody()?.toString()?.let {
				try {
					// TODO get message error
					message.append(JSONObject(it).getString("message"))
				} catch (e: JSONException) {
					message.append("\n")
				}
			}
			message.append("\nError code ${response.code()}")
			throw ApiException(message.toString())
		}
	}
}

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
