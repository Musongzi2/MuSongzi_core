package com.musongzi.test.vm

import androidx.lifecycle.SavedStateHandle
import com.musongzi.core.base.business.EmptyBusiness
import com.musongzi.core.base.manager.RetrofitManager
import com.musongzi.core.base.vm.DataDriveViewModel

/**
create by linhui , data = 2024/8/6 23:13
 **/
class KtTestViewModel(saved: SavedStateHandle? = null) : DataDriveViewModel<EmptyBusiness>(saved) {



    fun loaderData(){

//        RetrofitManager.getInstance().getApi()

    }


}