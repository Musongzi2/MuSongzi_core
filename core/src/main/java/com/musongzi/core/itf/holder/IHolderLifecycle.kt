package com.musongzi.core.itf.holder

import com.musongzi.core.itf.ILifeObject

@Deprecated("过时")
interface IHolderLifecycle : ILifeObject {

    fun getMainLifecycle(): IHolderLifecycle?

}