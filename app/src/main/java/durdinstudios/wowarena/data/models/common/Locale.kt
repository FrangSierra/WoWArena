package durdinstudios.wowarena.data.models.common

import com.squareup.moshi.Json

enum class Locale(val value: String) {
    @Json(name ="GB") ENGLISH("en_GB"),
    @Json(name ="DE") GERMAN("de_DE"),
    @Json(name ="ES") SPANISH("es_ES"),
    @Json(name ="FR") FRENCH("fr_FR"),
    @Json(name ="IT") ITALIAN("it_IT"),
    @Json(name ="PL") POLISH("pl_PL"),
    @Json(name ="PT") PORTUGUESE("pt_PT"),
    @Json(name ="RU") RUSSIAN("ru_RU"),
    @Json(name ="KR") KOREAN("ko_KR"),
    @Json(name ="TW") TAIWAN("zh_TW"),
    @Json(name ="US") USA("en_US"),
    @Json(name ="BR") BRASIL("pt_BR"),
    @Json(name ="MX") MEXICO("es_MX")
}