package durdinstudios.wowarena.data.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface WarcraftApi {

    @GET("leaderboard/{bracket}")
    fun getPvPLeaderboard(@Path("bracket") bracket: String,
                          @Body leaderboardBody: LeaderboardBody): Call<List<PlayerStats>>

    @GET("character/{realm}/{characterName}/")
    fun getPlayerPvpInfo(@Path("characterName") name: String,
                         @Path("realm") realm: String,
                         @Body body: PlayerPvpBody): Call<PlayerInfo>
}