package app.seals.sealsgallery.di

import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import org.koin.dsl.module

val domainDi = module {
    single {
        DrawTrack()
    }

    single {
        UpdateBounds()
    }
}