package durdinstudios.wowarena.domain.arena

import com.bq.masmov.reflux.dagger.AppScope
import durdinstudios.wowarena.data.WarcraftAPIInstances
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.profile.Character
import mini.Dispatcher
import javax.inject.Inject


interface ArenaController {

    fun getArenaStats(): Map<Character, Map<ArenaBracket, List<ArenaInfo>>>
    fun saveArenaStats(character: Character, bracket: ArenaBracket, info: ArenaInfo)
    fun deleteCharacterArenaInfo(character: Character)
}

@AppScope
class ArenaControllerImpl @Inject constructor(private val dispatcher: Dispatcher,
                                              private val arenaRepository: ArenaRepository,
                                              private val warcraftApi: WarcraftAPIInstances) : ArenaController {


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