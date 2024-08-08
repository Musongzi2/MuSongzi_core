package com.musongzi.test.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.musongzi.core.ExtensionCoreMethod.getApi
import com.musongzi.core.ExtensionCoreMethod.subAndSetData
import com.musongzi.core.base.business.EmptyBusiness
import androidx.lifecycle.DataDriveViewModel
import com.musongzi.test.MszTestApi

/**
create by linhui , data = 2024/7/31 20:09
 **/
class ListDataViewModel(saved: SavedStateHandle? = null) : DataDriveViewModel<EmptyBusiness>(saved) {


    companion object {

        const val DATA = "data"

    }

    fun loadDataUser() {
        Log.d(TAG, "loadDataUser: reday load getArrayEngine()")
        getApi<MszTestApi>().getArrayEngine(0, 3).subAndSetData(localSavedStateHandle,DATA)
    }


}