package com.musongzi.test.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.musongzi.core.ExtensionCoreMethod.saveStateChange
import com.musongzi.core.base.bean.IUserInfo
import com.musongzi.core.base.business.EmptyBusiness
import com.musongzi.core.base.vm.ApiViewModel
import com.musongzi.core.base.vm.DataDriveViewModel
import com.musongzi.test.bean.SongInfo
import com.musongzi.test.bean.UserInfo

/**
create by linhui , data = 2024/7/31 20:09
 **/
class ListDataViewModel(saved: SavedStateHandle? = null): DataDriveViewModel<EmptyBusiness>(saved) {

    companion object{

        const val DATA = "data"

    }

    fun loadDataUser(){
        Log.d(TAG, "loadDataUser: -->")
//        ApiViewModel

        DATA.saveStateChange(localSavedStateHandle, mutableListOf<UserInfo>().apply {
            add(UserInfo(System.currentTimeMillis(),"小米"))
            add(UserInfo(System.currentTimeMillis(),"小明"))
            add(UserInfo(System.currentTimeMillis(),"小白"))
            add(UserInfo(System.currentTimeMillis(),"小红", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小寻", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小志"))
            add(UserInfo(System.currentTimeMillis(),"小薰", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小强"))
            add(UserInfo(System.currentTimeMillis(),"小赞"))
            add(UserInfo(System.currentTimeMillis(),"小芸", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小欧"))
            add(UserInfo(System.currentTimeMillis(),"小林"))
            add(UserInfo(System.currentTimeMillis(),"小晨", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小婷", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小琪", gender = IUserInfo.WOMAN))
            add(UserInfo(System.currentTimeMillis(),"小国"))
            add(UserInfo(System.currentTimeMillis(),"小海"))
            add(UserInfo(System.currentTimeMillis(),"小痞"))
        })



    }




}