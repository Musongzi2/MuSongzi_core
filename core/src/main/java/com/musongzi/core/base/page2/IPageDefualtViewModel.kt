package com.musongzi.core.base.page2

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.musongzi.core.ExtensionCoreMethod.getSaveStateValue
import com.musongzi.core.ExtensionCoreMethod.liveSaveStateObserver
import com.musongzi.core.ExtensionCoreMethod.saveStateChange
import com.musongzi.core.base.bean.DefaultShowInfo
import com.musongzi.core.itf.holder.IHolderAnySaveStateHandler

interface IPageDefualtViewModel : IHolderAnySaveStateHandler {

    var state: Int
        set(value) {
            STATE_KEY.saveStateChange(this, value)
        }
        get() {
            return STATE_KEY.getSaveStateValue(this) ?: -1
        }

    var info: DefaultShowInfo?
        set(value) {
            STATE_INFO_KEY_L.saveStateChange(localSavedStateHandle, value)
        }
        get() {
            return STATE_INFO_KEY_L.getSaveStateValue(localSavedStateHandle)
        }

    fun observerState(lifecycleOwner: LifecycleOwner, observer: Observer<Int?>) {
        STATE_KEY.liveSaveStateObserver(lifecycleOwner, this, observer)
    }

    fun observerInfo(lifecycleOwner: LifecycleOwner, observer: Observer<DefaultShowInfo?>) {
        STATE_INFO_KEY_L.liveSaveStateObserver(lifecycleOwner, localSavedStateHandle, observer)
    }


    companion object{
        private const val STATE_KEY = "state_int_key"
        private const val STATE_INFO_KEY_L = "state_info_key"
    }

}