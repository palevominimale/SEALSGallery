package app.seals.sealsgallery.di

import app.seals.sealsgallery.ui.feed.FeedViewModel
import app.seals.sealsgallery.ui.main.MainActivityViewModel
import app.seals.sealsgallery.ui.mytracks.MyTracksViewModel
import app.seals.sealsgallery.ui.record.RecordViewModel
import app.seals.sealsgallery.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiDi = module {
    viewModel {
        FeedViewModel()
    }

    viewModel {
        RecordViewModel()
    }

    viewModel {
        MainActivityViewModel()
    }

    viewModel {
        MyTracksViewModel()
    }

    viewModel {
        SettingsViewModel()
    }
}