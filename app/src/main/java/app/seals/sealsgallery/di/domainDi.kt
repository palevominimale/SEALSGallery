package app.seals.sealsgallery.di

import app.seals.sealsgallery.domain.images.ImagesOperations
import app.seals.sealsgallery.domain.images.ImagesPicker
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.SetMarkers
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainDi = module {
    single {
        DrawTrack()
    }

    single {
        UpdateBounds()
    }

    single {
        SetMarkers(
            context = androidContext()
        )
    }

    single {
        ImagesPicker(
            context = androidContext()
        )
    }

    single {
        ImagesOperations()
    }
}