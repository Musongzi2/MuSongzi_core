package com.musongzi.spi

/*** created by linhui * on 2022/8/21 */
interface IStrategyRule {


    @JvmDefault
    fun onLoadRule(request: ISpiRequest):Class<*>? = request.getRequestLoaderClass()
}