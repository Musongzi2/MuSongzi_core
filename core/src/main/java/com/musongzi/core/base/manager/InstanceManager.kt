package com.musongzi.core.base.manager

import android.util.Log
import com.musongzi.core.itf.holder.IHodlerIdentity

/*** created by linhui * on 2022/7/29 */
interface InstanceManager : IHodlerIdentity {

//    fun managerId():Int = hashCode()

    fun onReady(a:Any?){
        Log.i(ManagerService.TAG, "onReady: ${javaClass.canonicalName}")
    }

    companion object
    {
        const val COMPLETE = 1.shl(25)
//        const val NO_ID = 0;
    }


}