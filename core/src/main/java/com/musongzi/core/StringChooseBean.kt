package com.musongzi.core

import com.musongzi.core.base.bean.BaseChooseBean
import com.musongzi.core.itf.IAttribute

open class StringChooseBean : BaseChooseBean() {

    var title = ""

    override fun toString(): String {
        return "[title = $title attributeId = $attributeId]"
    }

}