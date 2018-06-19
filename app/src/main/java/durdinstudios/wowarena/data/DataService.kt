package durdinstudios.wowarena.data

import android.content.Context
import com.bq.masmov.reflux.dagger.AppScope
import com.google.gson.Gson
import dagger.Lazy
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.R
import durdinstudios.wowarena.core.app
import durdinstudios.wowarena.data.models.common.Locale
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.Ranking
import durdinstudios.wowarena.data.network.NetworkDataSource
import durdinstudios.wowarena.data.network.UrlUtils.getBaseUrl
import durdinstudios.wowarena.data.network.WarcraftApi
import durdinstudios.wowarena.data.network.client.AuthorizationInterceptor
import durdinstudios.wowarena.data.persistence.DiskDataSource
import durdinstudios.wowarena.domain.user.UserStore
import durdinstudios.wowarena.domain.user.token.PreferencesTokenPersistenceImpl
import durdinstudios.wowarena.domain.user.token.TokenController
import mini.Dispatcher
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Interface to provide application data. It comes from disk or network.
 */
@AppScope
class DataService @Inject constructor(private val diskDataSource: DiskDataSource,
                                      private val networkDataSource: NetworkDataSource) : Service {

    override fun savePlayerInfo(playerInfo: PlayerInfo) {
        diskDataSource.savePlayerInfo(playerInfo)
    }

    override fun getPlayerInfoFromDisk(): PlayerInfo? {
        return diskDataSource.getPlayerInfo()
    }

    override fun getPlayerPvpInformation(realm: String, characterName: String, locale: String): PlayerInfo {
        return networkDataSource.getPlayerPvpInformation(realm, characterName, locale)
    }

    override fun getPvpLeaderboard(bracket: ArenaBracket): List<Ranking> {
        return networkDataSource.getPvpLeaderboard(bracket, Locale.SPANISH)
    }

}

/**
 * Error when trying to fetch an user from Battle net without Id info.
 */
class UserNotAvailableException : Throwable()

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class RepositoryModule(private val endpoint: String) {

    private val AUTHORIZATION = "Authorization"

    @Provides
    @AppScope
    fun provideDataRepository(diskDataSource: DiskDataSource,
                              networkDataSource: NetworkDataSource): Service =
        DataService(diskDataSource, networkDataSource)

    @Provides
    @AppScope
    fun provideTokenController(context: Context, gson: Gson): TokenController =
        TokenController(PreferencesTokenPersistenceImpl(context))

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
    fun provideRetrofit(client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(getBaseUrl(Region.EU)) //TODO make multi region
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @AppScope
    fun provideWarcraftApi(retrofit: Retrofit): WarcraftApi {
        return retrofit.create(WarcraftApi::class.java)
    }
}