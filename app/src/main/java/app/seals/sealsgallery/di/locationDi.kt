package app.seals.sealsgallery.di

import app.seals.sealsgallery.location.LocationService
import org.koin.dsl.module

val locationDi = module {
    single {
        LocationService(
        )
    }
}