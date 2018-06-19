package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName

enum class Locale(val value: String) {
    ENGLISH("en_GB"),
    GERMAN("de_DE"),
    SPANISH("es_ES"),
    FRENCH("fr_FR"),
    ITALIAN("it_IT"),
    POLISH("pl_PL"),
    PORTUGUESE("pt_PT"),
    RUSSIAN("ru_RU")
}