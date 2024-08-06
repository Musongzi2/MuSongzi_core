package com.musongzi.core.base

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.musongzi.core.BuildConfig
import com.musongzi.core.base.manager.InstanceManager
import com.musongzi.core.base.manager.ManagerInstanceHelp
import com.musongzi.core.base.manager.ManagerService.Companion.TAG
import com.musongzi.core.base.manager.ManagerUtil
import com.musongzi.core.base.manager.UncatchExcetionManager
import com.musongzi.core.itf.IHolderApplication
import com.musongzi.core.util.UiUtil
import java.util.Arrays
import java.util.LinkedHashSet

/*** created by linhui * on 2022/8/25 */
abstract class MszApplicaton : MultiDexApplication(), IHolderApplication {

    companion object {
        const val UNCATCH_MANAGER = "UncatchExcetionHelp"
    }

    final override fun onCreate() {
        super.onCreate()
//        ManagerUtil.
        ManagerUtil.init(getManagers().let {
            val h = LinkedHashSet<ManagerInstanceHelp>()
            if (enableWriteException() && isLoadingManagerByOtherProcess(UNCATCH_MANAGER)) {
                h.add(UncatchExcetionHelp())
            }
            h.addAll(it.filter { manager ->
                isLoadingManagerByOtherProcess(manager.key())
            })

            UiUtil.post{
                Log.d(TAG, "onCreate: managers -> ${Arrays.toString(h.toArray())}")
            }

            h
        }, getManagerClassLoader())
    }

    /**
     * this normal status is loading manager
     */
    protected fun isLoadingManagerByOtherProcess(managerName: String): Boolean = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .runningAppProcesses.find {
            it?.processName == packageName
        } != null

    protected fun enableWriteException(): Boolean {
        return BuildConfig.DEBUG;
    }

    protected open fun getManagerClassLoader(): ClassLoader {
        return classLoader
    }

    protected abstract fun getManagers(): Array<ManagerInstanceHelp>


    override fun getHolderContext(): Context {
        return this
    }

    internal class UncatchExcetionHelp : ManagerInstanceHelp {
        override fun instance(): InstanceManager? {
            return UncatchExcetionManager()
        }

        override fun toString()  = key()

        override fun key(): String {
            return "$UNCATCH_MANAGER ${hashCode()}"
        }
    }

}