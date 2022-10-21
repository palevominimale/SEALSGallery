package app.seals.sealsgallery.app

import android.app.Application
import app.seals.sealsgallery.di.*
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(
                uiDi,
                domainDi,
                firebaseDi,
                locationDi,
                dataDi,
                helpersDi
            ))
        }
    }
}