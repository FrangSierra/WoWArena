package durdinstudios.wowarena.data

import com.bq.masmov.reflux.dagger.AppScope
import dagger.Lazy
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.app
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.network.UrlUtils.getBaseUrl
import durdinstudios.wowarena.data.network.WarcraftApi
import durdinstudios.wowarena.data.network.client.AuthorizationInterceptor
import durdinstudios.wowarena.domain.user.UserStore
import mini.Dispatcher
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class RepositoryModule {

    private val AUTHORIZATION = "Authorization"

    @Provides
    @AppScope
    fun provideWarcraftService(warcraftApi: WarcraftAPIInstances): WarcraftService =
            WarcraftServiceImpl(warcraftApi)

    @Provides
    @AppScope
    fun provideHttpClient(userStore: Lazy<UserStore>,
                          dispatcher: Dispatcher): OkHttpClient {
        val interceptor = AuthorizationInterceptor(userStore, dispatcher)
        val client = OkHttpClient.Builder()
        client.addInterceptor(interceptor)
        client.authenticator { route, response ->
            val clientId = app.getString(R.string.BATTLE_NET_KEY)
            val clientSecret = app.getString(R.string.BATTLE_NET_SECRET)
            val credential = Credentials.basic(clientId, clientSecret)

            response.request().newBuilder()
                    .header(AUTHORIZATION, credential)
                    .build()
        }

        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Provides
    @AppScope
    @JvmSuppressWildcards
    fun provideRetrofitMap(client: OkHttpClient): Map<Region, Retrofit> {
        val map = hashMapOf<Region, Retrofit>()
        Region.values().forEach { region ->
            map.plusAssign(region to Retrofit.Builder().baseUrl(getBaseUrl(region))
                    .client(client).addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build())
        }
        return map
    }

    @Provides
    @AppScope
    @JvmSuppressWildcards
    fun provideWarcraftApi(retrofitInstances: Map<Region, Retrofit>): WarcraftAPIInstances {
        val map = hashMapOf<Region, WarcraftApi>()
        retrofitInstances.forEach { key, value -> map.plusAssign(key to value.create(WarcraftApi::class.java)) }
        return WarcraftAPIInstances(map)
    }
}

class WarcraftAPIInstances(val apis: Map<Region, WarcraftApi>)