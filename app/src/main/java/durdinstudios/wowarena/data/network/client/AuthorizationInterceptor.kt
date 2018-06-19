package durdinstudios.wowarena.data.network.client

import dagger.Lazy
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.app
import durdinstudios.wowarena.domain.user.UserStore
import mini.Dispatcher
import okhttp3.Interceptor
import okhttp3.Response

const val API_KEY = "apikey"

class AuthorizationInterceptor(private val lazyUserStore: Lazy<UserStore>,
                               private val dispatcher: Dispatcher) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY, app.getString(R.string.BATTLE_NET_KEY))
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}