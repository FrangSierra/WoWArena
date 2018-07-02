package durdinstudios.wowarena.domain.arena

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.WarcraftAPIInstances
import durdinstudios.wowarena.domain.arena.model.CharacterArenaStats
import durdinstudios.wowarena.domain.arena.model.toArenaInfo
import durdinstudios.wowarena.misc.taskFailure
import durdinstudios.wowarena.misc.taskSuccess
import durdinstudios.wowarena.profile.Character
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mini.Dispatcher
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface ArenaController {

    fun getArenaStats(): List<CharacterArenaStats>
    fun saveArenaStats(stats: CharacterArenaStats)
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
                        val stats = CharacterArenaStats(character, playerInfo.pvp.brackets.arena2v2?.toArenaInfo(),
                                playerInfo.pvp.brackets.arena3v3?.toArenaInfo(),
                                playerInfo.pvp.brackets.arenaRbg?.toArenaInfo(),
                                playerInfo.lastModified)
                        saveArenaStats(stats)
                    }
                    dispatcher.dispatchOnUi(DownloadArenaStatsComplete(taskSuccess()))
                }, { error ->
                    dispatcher.dispatchOnUi(DownloadArenaStatsComplete(taskFailure(error)))
                })
    }

    override fun getArenaStats(): List<CharacterArenaStats> {
        return arenaRepository.getArenaStats()
    }

    override fun saveArenaStats(stats: CharacterArenaStats) {
        arenaRepository.saveArenaStats(stats)
    }

    override fun deleteCharacterArenaInfo(character: Character) {
        arenaRepository.deleteCharacterArenaInfo(character)
    }
}