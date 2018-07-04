package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.profile.Character
import durdinstudios.wowarena.profile.CharacterInfo
import mini.Action

/** Action dispatched when we know if we have credentials or not */
data class LoadUserDataAction(val characterInfo: CharacterInfo) : Action

data class LoadUserDataCompleteAction(val playerInfo : PlayerInfo?,
                                      val character: Character?,
                                      val task: Task) : Action

data class LoadUserArenaDataCompleteAction(val characterInfo: CharacterInfo,
                                           val arenaStats: List<ArenaStats>,
                                           val task: Task) : Action

data class DeleteUserAction(val character: Character) : Action

data class DeleteUserCompleteAction(val character: Character,
                                    val task: Task) : Action

data class SetShow2vs2StatsSettingAction(val show: Boolean) : Action

data class SetShow3vs3StatsSettingAction(val show: Boolean) : Action

data class SetShowRbgStatsSettingAction(val show: Boolean) : Action