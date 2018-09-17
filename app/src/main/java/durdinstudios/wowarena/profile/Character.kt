package durdinstudios.wowarena.profile

import durdinstudios.wowarena.data.models.common.Race
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.common.WoWClass
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo

data class CharacterInfo(val username: String,
                         val realm: String,
                         val region: Region)

data class Character(val username: String,
                     val realm: String,
                     val region: Region,
                     val klass: WoWClass,
                     val race: Race,
                     val level: Int,
                     val thumbnail: String) {
    fun characterEqualsTo(username: String, realm: String) =
        this.username == username && this.realm == realm
}

fun Character.toCharacterInfo() = CharacterInfo(username = username,
    realm = realm,
    region = region)

fun PlayerInfo.toCharacter(region: Region) = Character(username = name,
    realm = realm,
    region = region,
    klass = gameClass,
    level = level,
    race = race,
    thumbnail = thumbnail)