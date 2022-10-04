package app.seals.sealsgallery.app

import android.app.Application
import app.seals.sealsgallery.di.domainDi
import app.seals.sealsgallery.di.firebaseDi
import app.seals.sealsgallery.di.locationDi
import app.seals.sealsgallery.di.uiDi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(
                uiDi,
                domainDi,
                firebaseDi,
                locationDi
            ))
        }
    }
}