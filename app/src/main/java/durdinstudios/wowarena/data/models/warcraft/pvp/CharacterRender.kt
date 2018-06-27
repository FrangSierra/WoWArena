package durdinstudios.wowarena.data.models.warcraft.pvp

import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.profile.Character

enum class RENDER_TYPE{
    AVATAR,
    MAIN,
    INSET;
}

private const val BASE_RENDER_URL = "http://render-%s.worldofwarcraft.com/character/%s"

fun PlayerInfo.getRenderUrl(region : Region): String {
    return BASE_RENDER_URL.format(region.name.toLowerCase(), this.thumbnail)
}

fun Character.getRenderUrl(region : Region): String {
    return BASE_RENDER_URL.format(region.name.toLowerCase(), this.thumbnail)
}