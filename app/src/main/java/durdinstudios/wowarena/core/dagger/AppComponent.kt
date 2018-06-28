package durdinstudios.wowarena.core.dagger

import android.app.Application
import android.content.Context
import com.bq.masmov.reflux.dagger.ActivityScope
import com.bq.masmov.reflux.dagger.AppScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import durdinstudios.wowarena.core.App
import durdinstudios.wowarena.core.SplashActivity
import durdinstudios.wowarena.core.flux.StoreHolderComponent
import durdinstudios.wowarena.data.RepositoryModule
import durdinstudios.wowarena.data.models.common.*
import durdinstudios.wowarena.domain.arena.ArenaModule
import durdinstudios.wowarena.domain.arena.ArenaRepositoryModule
import durdinstudios.wowarena.domain.leaderboard.LeaderboardModule
import durdinstudios.wowarena.domain.user.UserModule
import durdinstudios.wowarena.domain.user.UserRepositoryModule
import durdinstudios.wowarena.navigation.HomeActivity
import durdinstudios.wowarena.profile.AddCharacterActivity
import durdinstudios.wowarena.profile.CharacterListActivity
import durdinstudios.wowarena.profile.ProfileFragment
import durdinstudios.wowarena.ranking.BracketFragment
import durdinstudios.wowarena.ranking.RankingFragment
import durdinstudios.wowarena.settings.SettingsFragment
import mini.Dispatcher

/**
 * Main Dagger app component.
 */
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBindingsModule::class,
    AndroidSupportInjectionModule::class,
    UserModule::class,
    ArenaModule::class,
    ArenaRepositoryModule::class,
    UserRepositoryModule::class,
    LeaderboardModule::class,
    RepositoryModule::class,
    AppModule::class
])
@AppScope
@Suppress("UndocumentedPublicFunction")
interface AppComponent : StoreHolderComponent, AndroidInjector<App> {
    fun dispatcher(): Dispatcher
}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface ActivityBindingsModule {

    @ActivityScope
    @ContributesAndroidInjector
    fun mainActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun characterList(): CharacterListActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun addChar(): AddCharacterActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun splash(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun settingsFragment(): SettingsFragment

    @ActivityScope
    @ContributesAndroidInjector
    fun profileFragment(): ProfileFragment

    @ActivityScope
    @ContributesAndroidInjector
    fun bracketfragment(): BracketFragment

    @ActivityScope
    @ContributesAndroidInjector
    fun rankingFragment(): RankingFragment
}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class AppModule(val app: App) {

    @Provides
    @AppScope
    fun provideDispatcher() = Dispatcher()

    @Provides
    fun provideApplication(): Application = app

    @Provides
    fun provideAppContext(): Context = app


    @Provides
    @AppScope
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(ClassAdapter())
                .add(RegionAdapter())
                .add(FactionAdapter())
                .add(RaceAdapter())
                .add(GenderAdapter())
                .build()
    }
}