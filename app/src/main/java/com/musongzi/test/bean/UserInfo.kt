package com.musongzi.test.bean

import com.musongzi.core.base.bean.IUserInfo
import kotlinx.android.parcel.Parcelize

/**
create by linhui , data = 2024/7/31 21:36
 **/
data class UserInfo(
    val userId: Long,
    var name: String?,
    var age: Int = 18,
    var gender: Int = IUserInfo.MAN
) : IUserInfo {
    override fun getHolderUserName(): String? = name

    override fun getHolderUserNameId(): Long = userId
}