package com.musongzi.test.bean

import com.musongzi.core.base.bean.IUserInfo
import com.musongzi.core.itf.data.IChoose
import kotlinx.android.parcel.Parcelize

/**
create by linhui , data = 2024/7/31 21:36
 **/
data class UserInfo(
    val userId: Long,
    var name: String?,
    var age: Int = 18,
    var gender: Int = IUserInfo.MAN,
    private var choose:Boolean = false
) : IUserInfo,IChoose {
    override fun getHolderUserName(): String? = name

    override fun getHolderUserNameId(): Long = userId
    override fun isChoose(): Boolean {
        return choose
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) || userId == (other as? UserInfo)?.userId
    }

    override fun choose(b: Boolean) {
        choose = b
    }
}