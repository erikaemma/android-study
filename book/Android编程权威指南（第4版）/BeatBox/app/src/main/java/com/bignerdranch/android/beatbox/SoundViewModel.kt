package com.bignerdranch.android.beatbox

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData

class SoundViewModel(private val beatBox: BeatBox) : BaseObservable() {

    fun onButtonClicked() {
        sound?.let{
            beatBox.play(it)
        }
    }

    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()
        }

    @get:Bindable
    val title: String?
        get() = sound?.name
}

/* LiveData方案
 * 涉及到MainActivity的SoundAdapter.onCreateViewHolder
class SoundViewModel {

    val title: MutableLiveData<String?> = MutableLiveData()

    var sound: Sound? = null
        set(sound) {
            field = sound
            //notifyChange()
            title.postValue(sound?.name)
        }

    //@get:Bindable
    //val title: String?
    //    get() = sound?.name
}
*/