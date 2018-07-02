package durdinstudios.wowarena.domain.user.repository

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Inject


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface SettingsPersistence {
    fun getShow2vs2StatsSetting(): Boolean
    fun getShow3vs3StatsSetting(): Boolean
    fun getShowRbgStatsSetting(): Boolean
    fun setShow2vs2StatsSetting(newValue: Boolean)
    fun setShow3vs3StatsSetting(newValue: Boolean)
    fun setShowRbgStatsSetting(newValue: Boolean)
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsSettingsPersistence @Inject constructor(val context: Context) : SettingsPersistence {
    companion object {
        const val FILE = "settings_prefs"
        const val SHOW_2VS2_STATS = "show_2vs2_stats"
        const val SHOW_3VS3_STATS = "show_3vs3_stats"
        const val SHOW_RBG_STATS = "show_rbg_stats"
    }

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(FILE, Context.MODE_PRIVATE) }

    override fun setShow2vs2StatsSetting(newValue: Boolean) {
        prefs.edit().putBoolean(SHOW_2VS2_STATS, newValue).apply()
    }

    override fun setShow3vs3StatsSetting(newValue: Boolean) {
        prefs.edit().putBoolean(SHOW_3VS3_STATS, newValue).apply()
    }

    override fun setShowRbgStatsSetting(newValue: Boolean) {
        prefs.edit().putBoolean(SHOW_RBG_STATS, newValue).apply()
    }

    override fun getShow2vs2StatsSetting(): Boolean {
        return prefs.getBoolean(SHOW_2VS2_STATS, true)
    }

    override fun getShow3vs3StatsSetting(): Boolean {
        return prefs.getBoolean(SHOW_3VS3_STATS, true)
    }

    override fun getShowRbgStatsSetting(): Boolean {
        return prefs.getBoolean(SHOW_RBG_STATS, true)
    }
}

/**
 * Abstraction over user persistence.
 */
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SettingsRepository(private val settingsPersistence: SettingsPersistence) {
    fun getShow2vs2StatsSetting() = settingsPersistence.getShow2vs2StatsSetting()

    fun getShow3vs3StatsSetting() = settingsPersistence.getShow3vs3StatsSetting()

    fun getShowRbgStatsSetting() = settingsPersistence.getShowRbgStatsSetting()

    fun setShow2vs2StatsSetting(newValue: Boolean) {
        settingsPersistence.setShow2vs2StatsSetting(newValue)
    }

    fun setShow3vs3StatsSetting(newValue: Boolean) {
        settingsPersistence.getShow3vs3StatsSetting()
    }

    fun setShowRbgStatsSetting(newValue: Boolean) {
        settingsPersistence.setShowRbgStatsSetting(newValue)
    }
}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SettingsRepositoryModule {
    @Provides
    @AppScope
    fun provideSettingsRepository(settingsPersistence: SettingsPersistence): SettingsRepository {
        return SettingsRepository(settingsPersistence)
    }

    @Provides
    @AppScope
    fun provideSettingsPersistence(prefsSettingsPersistence: SharedPrefsSettingsPersistence): SettingsPersistence {
        return prefsSettingsPersistence
    }
}
