package com.musongzi.core.itf

import com.trello.rxlifecycle4.LifecycleTransformer

interface IWant  {

    fun <T> bindToLifecycle(): LifecycleTransformer<T>?

}