package com.musongzi.test.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.musongzi.core.base.business.EmptyBusiness
import com.musongzi.core.base.vm.ApiViewModel
import com.musongzi.core.base.vm.DataDriveViewModel

/**
create by linhui , data = 2024/7/31 20:09
 **/
class ListDataViewModel(saved: SavedStateHandle? = null): DataDriveViewModel<EmptyBusiness>(saved) {


    fun loadDataUser(){
        Log.d(TAG, "loadDataUser: -->")
//        ApiViewModel

    }




}