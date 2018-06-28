package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class Gender(val value: Int) {

    MALE(0),
    FEMALE(1);

    companion object {
        operator fun get(letter: String): Gender? =
                when {
                    "m".equals(letter, ignoreCase = true) -> MALE
                    "f".equals(letter, ignoreCase = true) -> FEMALE
                    else -> null
                }
    }
}


class GenderAdapter {
    @ToJson
    fun toJson(gender: Gender): Int {
        return gender.value
    }

    @FromJson
    fun fromJson(value: Int): Gender {
        return fromInt(value)
    }

    fun fromInt(value: Int): Gender {
        return Gender.values().first { it.value == value }
    }
}