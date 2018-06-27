package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName

/**
 * Represents the different regions in the Battle.net API
 */
enum class Region {
    @SerializedName("0")
    EU,
    @SerializedName("1")
    US;
}