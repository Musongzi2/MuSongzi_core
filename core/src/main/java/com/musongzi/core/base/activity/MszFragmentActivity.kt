package com.musongzi.core.base.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.musongzi.core.base.business.SupproActivityBusiness
import com.musongzi.core.base.business.itf.ISupprotActivityBusiness
import com.musongzi.core.itf.holder.IHolderContext

/*** created by linhui * on 2022/7/6 */
open class MszFragmentActivity : AppCompatActivity() ,IHolderContext{

    lateinit var business: ISupprotActivityBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        business = SupproActivityBusiness.create(savedInstanceState,this)
    }

    override fun getHolderContext() = this





}