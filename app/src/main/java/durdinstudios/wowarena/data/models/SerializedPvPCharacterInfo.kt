package durdinstudios.wowarena.data.models

data class SerializedPvPCharacterInfo(
    val ranking: Long,
    val rating: Int,
    val name: String,
    val realmId: Long,
    val realmName: String,
    val realmSlug: String,
    val raceId: Int,
    val classId: Int,
    val specId: Int,
    val factionId: Int,
    val genderId: Int,
    val seasonsWins: Long,
    val seasonLosses: Long,
    val weeklyWins: Long,
    val weeklyLosses: Long
)
