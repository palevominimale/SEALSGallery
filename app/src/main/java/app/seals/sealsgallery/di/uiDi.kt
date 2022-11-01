package app.seals.sealsgallery.di

import android.app.Activity
import app.seals.sealsgallery.ui.feed.main.FeedViewModel
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragment
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragmentViewModel
import app.seals.sealsgallery.ui.main.MainActivity
import app.seals.sealsgallery.ui.main.MainActivityViewModel
import app.seals.sealsgallery.ui.mytracks.MyTracksViewModel
import app.seals.sealsgallery.ui.mytracks_feed_based.MyTracksFeedViewModel
import app.seals.sealsgallery.ui.record.RecordViewModel
import app.seals.sealsgallery.ui.settings.SettingsViewModel
import app.seals.sealsgallery.ui.settings_extended.SettingsFragmentExtendedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiDi = module {
    viewModel {
        FeedViewModel(
            context = androidContext(),
            feedRepository = get()
        )
    }

    viewModel {
        ShowFeedItemFragmentViewModel(
            drawTrack = get(),
            updateBounds = get(),
            setStartEndMarkers = get(),
            showPostModel = get()
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
        MyTracksFeedViewModel(
            context = androidContext(),
            feedRepository = get()
        )
    }

    viewModel {
        SettingsFragmentExtendedViewModel()
    }

    viewModel {
        SettingsViewModel()
    }

    single <Activity> {
        MainActivity()
    }

    single {
        ShowFeedItemFragment()
    }
}