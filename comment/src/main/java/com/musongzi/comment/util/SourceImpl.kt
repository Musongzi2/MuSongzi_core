package com.musongzi.comment.util

import com.musongzi.core.itf.page.ISource


class SourceImpl<Item>(var datas: MutableList<Item> = mutableListOf()) : ISource<Item> {

    override fun realData() = datas
}