package durdinstudios.wowarena.core

import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import durdinstudios.wowarena.BuildConfig
import durdinstudios.wowarena.core.dagger.AppComponent
import durdinstudios.wowarena.core.dagger.AppModule
import durdinstudios.wowarena.core.dagger.DaggerAppComponent
import durdinstudios.wowarena.data.RepositoryModule
import io.fabric.sdk.android.Fabric
import mini.DebugTree
import mini.Grove
import mini.MiniActionReducer
import mini.initStores
import mini.log.LoggerInterceptor
import org.jetbrains.annotations.TestOnly
import kotlin.properties.Delegates

/**
 * Created by fragarsie on 12/18/17.
 */

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

/**
 * Global application object.
 */
class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return component
    }

    val exceptionHandlers: MutableList<Thread.UncaughtExceptionHandler> = ArrayList()

    val component: AppComponent
        get() {
            if (componentInstance == null) {
                componentInstance = DaggerAppComponent.builder()
                        .appModule(AppModule(app))
                        .repositoryModule(RepositoryModule())
                        .build()
            }
            return componentInstance!!
        }
    private var componentInstance: AppComponent? = null

    override fun onCreate() {
        appInstance = this
        super.onCreate()

        if (BuildConfig.DEBUG) Grove.plant(DebugTree(true))

        Fabric.with(this, Crashlytics())

        val stores = componentInstance!!.stores()
        componentInstance!!.dispatcher().actionReducers.add(MiniActionReducer(stores = stores))
        componentInstance!!.dispatcher().addInterceptor(LoggerInterceptor(stores = stores.values, logInBackground = false))

        initStores(componentInstance!!.stores().values)

        component.inject(this)
    }

    @TestOnly
    @Suppress("UndocumentedPublicFunction")
    fun setAppComponent(appComponent: AppComponent) {
        componentInstance = appComponent
    }
}
