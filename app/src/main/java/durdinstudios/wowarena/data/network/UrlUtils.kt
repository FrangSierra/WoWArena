package durdinstudios.wowarena.data.network

import android.net.Uri
import durdinstudios.wowarena.data.models.common.Region

object UrlUtils {

    private val BASE_URL = "https://%s.api.battle.net"
    private val BASE_URL_CN = "https://cn.battle.net"

    private val AUTHORIZE_URI = "https://%s.battle.net/oauth/authorize"
    private val AUTHORIZE_URI_CN = "https://www.battlenet.com.cn/oauth/authorize"

    private fun getAuthorizeUri(region: Region): String = when (region) {
        Region.EU, Region.US -> String.format(AUTHORIZE_URI, region.name.toLowerCase())
        //Region.EU, Region.US, Region.KR, Region.TW -> String.format(AUTHORIZE_URI, region.name.toLowerCase())
    }

    fun getBaseUrl(region: Region): String = when (region) {
        Region.EU, Region.US -> String.format(BASE_URL, region.name.toLowerCase())
        //Region.EU, Region.US, Region.KR, Region.TW -> String.format(BASE_URL, region.name.toLowerCase())
    }

    fun getAuthorizationUrl(region: Region, clientId: String, redirectUri: String): String {

        return Uri.parse(getAuthorizeUri(region))
                .buildUpon()
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("response_type", Constants.CODE)
                .build().toString()
    }
}