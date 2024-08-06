package com.musongzi.core.base.manager

/*** created by linhui * on 2022/8/1 */
interface ManagerInstanceHelp {

    fun instance(): InstanceManager?

    fun classLoadPathName(): String? = null

    fun readyNow(my: InstanceManager): Any? = null

    fun key(): String

    companion object {


        const val NORMAL_MANAGER_CLASS = "ManagerInstanceHelpImpl"

        @JvmOverloads
        fun instanceHelp(name: String? = null,instance: () -> InstanceManager): ManagerInstanceHelp {
            return object : ManagerInstanceHelp {

                override fun instance(): InstanceManager? {
                    return instance.invoke()
                }

                override fun key(): String {
                    return "$name ${hashCode()}"
                }

                override fun toString() = key()

            }
        }

        @JvmOverloads
        fun instanceOnReady(name: String? = null , runnable: Runnable ): ManagerInstanceHelp {
            return object : ManagerInstanceHelp {
                override fun instance(): InstanceManager? {
                    return InstanceManagerSimple(runnable)
                }

                override fun key(): String {
                    return "$name ${hashCode()}"
                }

                override fun toString() = key()
            }
        }

    }


}