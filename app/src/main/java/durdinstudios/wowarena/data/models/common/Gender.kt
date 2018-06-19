package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName

enum class Gender {

    @SerializedName("0") MALE,
    @SerializedName("1") FEMALE;

    companion object {
        operator fun get(letter: String): Gender? =
            when {
                "m".equals(letter, ignoreCase = true) -> MALE
                "f".equals(letter, ignoreCase = true) -> FEMALE
                else -> null
            }
    }
}