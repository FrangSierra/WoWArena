package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle
import durdinstudios.wowarena.profile.Character
import durdinstudios.wowarena.settings.Settings

@Suppress("UndocumentedPublicClass")
data class UserState(
        val playersInfo: Map<CharacterInfo, PlayerInfo> = emptyMap(),
        val selectedCharacter: Character?,
        val settings: Settings = Settings(),
        val currentCharacters: List<Character> = emptyList(),
        val currentRegion: Region = Region.EU,
        val loadUserTask: Task = taskIdle(),
        val deleteTask: Task = taskIdle())

typealias CharacterInfo = Pair<String,String>