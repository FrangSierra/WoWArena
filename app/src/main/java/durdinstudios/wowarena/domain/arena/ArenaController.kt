package durdinstudios.wowarena.domain.arena

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.WarcraftAPIInstances
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.domain.arena.model.toArenaInfo
import durdinstudios.wowarena.misc.taskFailure
import durdinstudios.wowarena.misc.taskSuccess
import durdinstudios.wowarena.profile.Character
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import javax.inject.Inject


interface ArenaController {

    fun getArenaStats(): Map<Character, Map<ArenaBracket, List<ArenaInfo>>>
    fun saveArenaStats(character: Character, bracket: ArenaBracket, info: ArenaInfo)
    fun deleteCharacterArenaInfo(character: Character)
    fun downloadArenaStats(currentCharacters: List<Character>)
}

@AppScope
class ArenaControllerImpl @Inject constructor(private val dispatcher: Dispatcher,
                                              private val arenaRepository: ArenaRepository,
                                              private val warcraftApi: WarcraftAPIInstances) : ArenaController {

    override fun downloadArenaStats(currentCharacters: List<Character>) {
        val databaseCalls = currentCharacters.map { warcraftApi.apis[it.region]?.getPlayerPvpInfo(it.username, it.realm) }
        Single.merge(databaseCalls)
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe({ data ->
                    data.forEach { playerInfo ->
                        val character = currentCharacters
                                .first { it.username == playerInfo.name && it.realm == playerInfo.realm }
                        playerInfo.pvp.brackets.arena2v2?.let {
                            arenaRepository.saveArenaStats(character, ArenaBracket.BRACKET_2_VS_2, it.toArenaInfo())
                        }
                        playerInfo.pvp.brackets.arena3v3?.let {
                            arenaRepository.saveArenaStats(character, ArenaBracket.BRACKET_3_VS_3, it.toArenaInfo())
                        }
                        playerInfo.pvp.brackets.arenaRbg?.let {
                            arenaRepository.saveArenaStats(character, ArenaBracket.RBG, it.toArenaInfo())
                        }
                    }
                    dispatcher.dispatchOnUi(DownloadArenaStatsComplete(taskSuccess()))
                }, { error ->
                    dispatcher.dispatchOnUi(DownloadArenaStatsComplete(taskFailure(error)))
                })
    }

    override fun getArenaStats(): Map<Character, Map<ArenaBracket, List<ArenaInfo>>> {
        return arenaRepository.getArenaStats()
    }

    override fun saveArenaStats(character: Character, bracket: ArenaBracket, info: ArenaInfo) {
        arenaRepository.saveArenaStats(character, bracket, info)
    }

    override fun deleteCharacterArenaInfo(character: Character) {
        arenaRepository.deleteCharacterArenaInfo(character)
    }
}