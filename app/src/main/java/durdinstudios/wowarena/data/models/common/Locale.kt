package durdinstudios.wowarena.data.models.common

import com.google.gson.annotations.SerializedName

enum class Locale(val value: String) {
    @SerializedName("GB") ENGLISH("en_GB"),
    @SerializedName("DE") GERMAN("de_DE"),
    @SerializedName("ES") SPANISH("es_ES"),
    @SerializedName("FR") FRENCH("fr_FR"),
    @SerializedName("IT") ITALIAN("it_IT"),
    @SerializedName("PL") POLISH("pl_PL"),
    @SerializedName("PT") PORTUGUESE("pt_PT"),
    @SerializedName("RU") RUSSIAN("ru_RU"),
    @SerializedName("KR") KOREAN("ko_KR"),
    @SerializedName("TW") TAIWAN("zh_TW"),
    @SerializedName("US") USA("en_US"),
    @SerializedName("BR") BRASIL("pt_BR"),
    @SerializedName("MX") MEXICO("es_MX")
}