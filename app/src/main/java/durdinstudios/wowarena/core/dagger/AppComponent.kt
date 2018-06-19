package durdinstudios.wowarena.core.dagger

import android.app.Application
import android.content.Context
import com.bq.masmov.reflux.dagger.ActivityScope
import com.bq.masmov.reflux.dagger.AppScope
import com.google.gson.Gson
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import durdinstudios.wowarena.MainActivity
import durdinstudios.wowarena.core.App
import durdinstudios.wowarena.core.flux.StoreHolderComponent
import durdinstudios.wowarena.data.RepositoryModule
import durdinstudios.wowarena.domain.leaderboard.LeaderboardModule
import durdinstudios.wowarena.domain.user.UserModule
import mini.Dispatcher

/**
 * Main Dagger app component.
 */
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBindingsModule::class,
    AndroidSupportInjectionModule::class,
    UserModule::class,
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
    fun mainActivity(): MainActivity
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
    fun provideGson() = Gson()
}