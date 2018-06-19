package durdinstudios.wowarena.data.network.client

import dagger.Lazy
import durdinstudios.wowarena.domain.user.UserStore
import mini.Dispatcher
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

const val AUTHORIZATION_HEADER = "Authorization"

class AuthorizationInterceptor(private val lazyUserStore: Lazy<UserStore>,
                               private val dispatcher: Dispatcher) : Interceptor {

    val userStore: UserStore by lazy { lazyUserStore.get() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val originalToken = userStore.state.token
            ?: return chain.proceed(chain.request())

        val requestWithAuthToken = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer $originalToken")
            .method(request.method(), request.body())
            .build()

        val response = chain.proceed(requestWithAuthToken)

        return response
        // return if (needsRefresh(response)) {
        //     val newAuthToken: String? = performRefresh(originalToken)
        //     return retryRequest(request, newAuthToken, chain)
        // } else {
        //     response
        // }
    }

//    private fun performRefresh(originalToken: String): String? {
//        synchronized(this) {
//            //Check whether the token was refreshed by another concurrent request
//            val currentToken = userStore.state.token
//
//            return if (originalToken == currentToken) {
//                val refreshToken = refreshController.refreshToken(originalToken)
//                val customer = if (refreshToken != null) userStore.state.player else null
//                val task: Task = if (refreshToken != null) taskSuccess() else taskFailure()
//
//                dispatcher.dispatchOnUiSync(TokenRefreshedAction(
//                    token = refreshToken,
//                    customer = customer,
//                    refreshTask = task))
//
//                refreshToken
//            } else {
//                userStore.state.token
//            }
//        }
//    }

    private fun retryRequest(request: Request,
                             newAuthToken: String?,
                             chain: Interceptor.Chain): Response {
        val requestWithAuthTokenRefreshed = request.newBuilder()
            .apply {
                newAuthToken?.let {
                    addHeader(AUTHORIZATION_HEADER, "Bearer $newAuthToken")
                }
            }.build()
        return chain.proceed(requestWithAuthTokenRefreshed)
    }

    //TODO check other cases
    private fun needsRefresh(response: Response): Boolean {
        return response.code() == 401
    }
}