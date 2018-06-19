package durdinstudios.wowarena.data.network

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.common.Locale
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.Ranking
import mini.Grove
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Data source that requests data to Lumiere.
 */
@AppScope
class NetworkDataSource @Inject constructor(private val warcraftApi: WarcraftApi,
                                            private val retrofit: Retrofit) {

    @Throws(DataServiceException::class)
    fun getPvpLeaderboard(bracket: ArenaBracket, locale: Locale): List<Ranking> =
        BattleNetCall {
            warcraftApi.getPvPLeaderboard(bracket.value, locale.name).execute().body()!!
        }

    @Throws(DataServiceException::class)
    fun getPlayerPvpInformation(realm: String, characterName: String, locale: String): PlayerInfo =
        BattleNetCall {
            warcraftApi.getPlayerPvpInfo(characterName, realm, locale = locale).execute().body()!!
        }

    private fun parseError(throwable: HttpException): ApiError {
        val converter = retrofit.responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls(0))
        return try {
            converter.convert(throwable.response().errorBody()!!)
        } catch (e: Exception) {
            ApiError("UNKNOWN", "Failed to convert error from API", "No message available")
        }
    }

    @Throws(DataServiceException::class)
    private fun <T> BattleNetCall(body: () -> T): T {
        return try {
            body()
        } catch (e: HttpException) {
            val apiError = parseError(e)
            Grove.e { apiError.toString() }
            throw DataServiceException(apiError)
        } catch (e: Exception) {
            val error = GenericError(e)
            throw DataServiceException(error)
        }
    }
}