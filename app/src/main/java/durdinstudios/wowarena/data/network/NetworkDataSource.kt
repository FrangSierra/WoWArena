package durdinstudios.wowarena.data.network

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.models.ArenaBracket
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
    fun getPvpLeaderboard(bracket: ArenaBracket): List<PlayerStats> =
        BattleNetCall {
            val body = LeaderboardBody("en_ES")
            warcraftApi.getPvPLeaderboard(bracket.value, body).execute().body()!!
        }

    @Throws(DataServiceException::class)
    fun getPlayerPvpInformation(realm: String, characterName: String, locale: String): PlayerInfo =
        BattleNetCall {
            val body = PlayerPvpBody(locale = locale)
            warcraftApi.getPlayerPvpInfo(characterName, realm, body).execute().body()!!
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