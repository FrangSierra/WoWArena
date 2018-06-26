package durdinstudios.wowarena.domain.user

import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import durdinstudios.wowarena.misc.Task
import mini.Action

/** Action dispatched when we know if we have credentials or not */
data class LoadUserDataAction(val nick: String,
                              val realm: String) : Action

data class LoadUserDataCompleteAction(val info: PlayerInfo?,
                                      val userDataTask: Task) : Action