package durdinstudios.wowarena.data.networking

import durdinstudios.wowarena.data.models.SerializedPvPCharacterInfoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PvPLeaderboardsService {
    @GET("{bracket}?")
    fun renameFile(@Path("bracket") bracket: String): Single<SerializedPvPCharacterInfoResponse>
}