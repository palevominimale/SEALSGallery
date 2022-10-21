package app.seals.sealsgallery.di

import app.seals.sealsgallery.ui.feed.main.FeedViewModel
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragment
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragmentViewModel
import app.seals.sealsgallery.ui.main.MainActivity
import app.seals.sealsgallery.ui.main.MainActivityViewModel
import app.seals.sealsgallery.ui.mytracks.MyTracksViewModel
import app.seals.sealsgallery.ui.record.RecordViewModel
import app.seals.sealsgallery.ui.settings.SettingsViewModel
import app.seals.sealsgallery.ui.settings_extended.SettingsFragmentExtendedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiDi = module {
    viewModel {
        FeedViewModel(
            drawTrack = get(),
            updateBounds = get(),
            context = androidContext(),
            feedRepository = get()
        )
    }

    viewModel {
        ShowFeedItemFragmentViewModel(
            context = androidContext(),
            drawTrack = get(),
            updateBounds = get(),
            setStartEndMarkers = get()
        )
    }

    viewModel {
        RecordViewModel(
            context = androidContext(),
            drawTrack = get(),
            updateBounds = get()
        )
    }

    viewModel {
        MainActivityViewModel(
            context = androidContext()
        )
    }

    viewModel {
        MyTracksViewModel(
            context = androidContext(),
            drawTrack = get(),
            updateBounds = get(),
            roomDB = get(),
            imagesPicker = get(),
            imagesOperations = get()
        )
    }

    viewModel {
        SettingsFragmentExtendedViewModel()
    }

    viewModel {
        SettingsViewModel()
    }

    single {
        MainActivity()
    }

    single {
        ShowFeedItemFragment()
    }
}