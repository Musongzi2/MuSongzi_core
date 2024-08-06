package com.musongzi.core.base.manager

import com.musongzi.core.itf.holder.IHodlerIdentity

/*** created by linhui * on 2022/8/25 */
class InstanceManagerSimple constructor(
    var id: Int,
    private val ready: Runnable
) : InstanceManager {

    constructor(ready: Runnable) : this(0, ready)

//    override fun managerId(): Int {
//        return if (id == InstanceManager.NO_ID) super.managerId() else id
//    }

    override fun onReady(a: Any?) {
        super.onReady(a)
        ready.run()
    }

    override val otherHodlerIdentity: IHodlerIdentity? = null

    override val holderIdentityName: String = id.toString()


}