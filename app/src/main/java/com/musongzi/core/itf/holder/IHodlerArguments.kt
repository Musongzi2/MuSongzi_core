package com.musongzi.core.itf.holder

interface IHodlerArguments<D> : ILifeObject {

    fun putArguments(d: D?);

    fun getArguments(): D?

    fun handlerArguments()


}