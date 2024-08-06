package com.musongzi.test

import android.util.Log
import com.musongzi.spi.ISpiRequest
import com.musongzi.spi.IStrategyRule
import com.musongzi.test.spi.MyTestSpiImp

/*** created by linhui * on 2022/8/21
 *
 * 动态加载配置，配合
 *
 * */
class MyRuleProxy : IStrategyRule {


    override fun onLoadRule(request: ISpiRequest): Class<*>? {

        when (request.orderName()) {
            "MyTestSpiImp" -> {
              return  NewTest::class.java
            }
        }
        return super.onLoadRule(request);
    }

    internal class NewTest : MyTestSpiImp.Test() {

        override fun hello() {
            Log.d("MyTestSpiImp", "hello: 222->")
        }

    }

}