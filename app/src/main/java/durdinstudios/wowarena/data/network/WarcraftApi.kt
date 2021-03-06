package durdinstudios.wowarena.data.network

import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.data.models.warcraft.pvp.Ranking
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WarcraftApi {

    @GET("wow/leaderboard/{bracket}")
    fun getPvPLeaderboard(@Path("bracket") bracket: String,
                          @Query("locale") locale: String? = null): Single<Ranking>

    @GET("wow/character/{realm}/{characterName}")
    fun getPlayerPvpInfo(@Path("characterName") name: String,
                         @Path("realm") realm: String,
                         @Query("fields") fields: String = "pvp",
                         @Query("locale") locale: String? = null): Single<PlayerInfo>
}