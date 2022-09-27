package app.seals.sealsgallery.ui.mytracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyTracksViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is My tracks Fragment"
    }
    val text: LiveData<String> = _text
}