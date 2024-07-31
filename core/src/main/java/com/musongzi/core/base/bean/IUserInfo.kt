package com.musongzi.core.base.bean

interface IUserInfo {

    fun getHolderUserName(): String?
    fun getHolderUserNameId(): Long

    companion object {

        const val MAN = 1

        const val WOMAN = 2

    }

}