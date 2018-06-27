package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.profile.Character
import mini.Action

/** Action dispatched when we know if we have credentials or not */
data class LoadUserDataAction(val nick: String,
                              val realm: String,
                              val region: Region) : Action

data class LoadUserDataCompleteAction(val info: PlayerInfo?,
                                      val character: Character?,
                                      val task: Task) : Action

data class DeleteUserAction(val nick: String,
                            val realm: String) : Action