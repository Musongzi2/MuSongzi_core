package com.musongzi.test.vm

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.musongzi.core.ExtensionCoreMethod.saveStateChange
import com.musongzi.core.base.bean.IUserInfo
import com.musongzi.core.base.business.EmptyBusiness
import com.musongzi.core.base.page2.PageCallBack
import com.musongzi.core.base.page2.PageLoader
import com.musongzi.core.base.page2.RequestObservableBean
import com.musongzi.core.base.page2.SimplePageCall
import com.musongzi.core.base.vm.ApiViewModel
import com.musongzi.core.base.vm.DataDriveViewModel
import com.musongzi.core.itf.page.IPageEngine
import com.musongzi.test.bean.ListDataBean
import com.musongzi.test.bean.SongInfo
import com.musongzi.test.bean.UserInfo
import io.reactivex.rxjava3.core.Observable

/**
create by linhui , data = 2024/7/31 20:09
 **/
class ListDataViewModel(saved: SavedStateHandle? = null) :
    DataDriveViewModel<EmptyBusiness>(saved) {





    companion object {

        const val DATA = "data"

    }

    fun loadDataUser(lifecycleOwner: LifecycleOwner) {
        Log.d(TAG, "loadDataUser: -->")
    }




}