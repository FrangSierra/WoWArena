package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle
import durdinstudios.wowarena.profile.Character

@Suppress("UndocumentedPublicClass")
data class UserState(
        val playersInfo: Map<CharacterInfo, PlayerInfo> = emptyMap(),
        val selectedCharacter: Character?,
        val currentCharacters: List<Character> = emptyList(),
        val currentRegion: Region = Region.EU,
        val loadUserTask: Task = taskIdle())

typealias CharacterInfo = Pair<String,String>