package app.seals.sealsgallery.di

import app.seals.sealsgallery.data.repos.TracksRepositoryImpl
import app.seals.sealsgallery.data.repos.UserRepositoryImpl
import app.seals.sealsgallery.domain.interfaces.TrackRepository
import app.seals.sealsgallery.domain.interfaces.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataDi = module {
    single<TrackRepository> {
        TracksRepositoryImpl(
            context = androidContext()
        )
    }

    single <UserRepository>{
        UserRepositoryImpl(
            context = androidContext()
        )
    }
}