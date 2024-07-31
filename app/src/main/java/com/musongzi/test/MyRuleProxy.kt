package com.musongzi.test

import com.musongzi.spi.ISpiRequest
import com.musongzi.spi.IStrategyRule

/*** created by linhui * on 2022/8/21
 *
 * 动态加载配置，配合
 *
 * */
class MyRuleProxy : IStrategyRule {


    override fun onLoadRule(request: ISpiRequest): Class<*> {

        TODO("error")

    }


}