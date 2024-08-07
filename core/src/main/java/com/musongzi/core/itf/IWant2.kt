package com.musongzi.core.itf

import java.io.Closeable

interface IWant2 :IViewInstance{

    fun addOnClose(close:Closeable)

}