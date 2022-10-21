package app.seals.sealsgallery.di

import app.seals.sealsgallery.ui.helpers.ShowPostModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val helpersDi = module {

    factory {
        ShowPostModel(
            context = androidContext(),
            activity = get(),
            drawTrack = get(),
            setStartEndMarkers = get(),
            updateBounds = get()
        )
    }
}