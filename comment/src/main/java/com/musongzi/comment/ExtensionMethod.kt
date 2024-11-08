package com.musongzi.comment

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.musongzi.comment.business.DoubleLimiteBusiness
import com.musongzi.comment.util.ApkUtil
import com.musongzi.core.StringChooseBean
import com.musongzi.core.annotation.CollecttionsEngine
import com.musongzi.comment.activity.MszFragmentActivity
import com.musongzi.core.base.bean.BusinessInfo
import com.musongzi.core.base.bean.FragmentDescribe
import com.musongzi.core.base.bean.StyleMessageDescribe
import com.musongzi.comment.business.SupproActivityBusiness
import com.musongzi.core.base.bean.ActivityDescribe
import com.musongzi.core.base.business.itf.IHolderSupportActivityBusiness
import com.musongzi.core.base.fragment.MszFragment
import com.musongzi.core.base.manager.ActivityLifeManager
import com.musongzi.core.itf.*
import com.musongzi.core.itf.holder.IHolderSavedStateHandler
import com.musongzi.core.itf.page.IPageEngine
import com.musongzi.core.util.ActivityThreadHelp
import com.musongzi.core.util.ActivityThreadHelp.getCurrentApplication
import com.musongzi.core.util.InjectionHelp
import com.musongzi.core.util.ScreenUtil
import io.reactivex.rxjava3.core.Observable

/**
 * 一个提供扩展方法的地方
 */
object ExtensionMethod {

    fun String.bean() = StringChooseBean().let {
        it.title = this
        it
    }

    /**
     * 通过fragment直接打开一个activity
     * @param activity 框架内的一个[MszFragmentActivity]activity。继承于此的任何子类都可以
     * @param mStyleMessageDescribe 控制样式一些信息，比如标题，状态栏颜色
     * @param dataBundle 传递的数据
     * @param businessClassName 如果fragment继承于 [MszFragment] 此注入可以控制当前 viewmodel 的业务的business初始化类型
     *                          请注意，一定要是相关的继承关系
     */
    @JvmStatic
    @JvmOverloads
    fun <F : Fragment> Class<F>.startActivityNormal(
        activity: Class<*>? = null,
        mStyleMessageDescribe: StyleMessageDescribe,
        dataBundle: Bundle? = null,
        businessClassName: String? = null
    ) {
        (ActivityLifeManager.getInstance().getTopActivity() ?: getCurrentApplication()).let {
            val activityClass = activity ?: MszFragmentActivity::class.java;
            val intent = Intent(it, activityClass)
            val fInfo = FragmentDescribe(
                this.name,
                mStyleMessageDescribe,
                if (businessClassName != null) BusinessInfo(businessClassName) else null
            )
            intent.putExtra(
                SupproActivityBusiness.ACTIVITY_DESCRIBE_INFO_KEY,
                ActivityDescribe(activityClass.name, fInfo)
            )
            dataBundle?.let { b ->
                intent.putExtra(SupproActivityBusiness.BUNDLE_KEY, b)
            }
            if (it is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            it.startActivity(intent)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <F : Fragment> Class<F>.startActivityNormal(
        title: String? = null,
        //其实必须是NormalFragmentActivity 子类
        activity: Class<*>? = MszFragmentActivity::class.java,
        barColor: Int = R.color.bg_white,
        dataBundleMethod: ((Bundle) -> Unit)? = null,
        businessClassName: String? = null
    ) {
        val bundle = Bundle()
        if(dataBundleMethod != null){
            dataBundleMethod(bundle)
        }
        startActivityNormal(
            activity,
            StyleMessageDescribe(title, barColor),
            bundle,
            businessClassName
        )
    }

    /**
     * 打开一个activity
     */
    fun <A : Activity> Class<A>.startActivity() {
        (ActivityLifeManager.getInstance().getTopActivity() ?: getCurrentApplication()).let {
            try {
                val i = Intent(it, this);
                if (it is Application) {
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                it.startActivity(i)
            } catch (e: Exception) {
                toast("无法打开${this.canonicalName}活动~", null)
                e.printStackTrace()
            }
        }
    }

    fun toast(msg: String?, activity: Activity? = null, cacheKey: String? = "TOAST_KEY") {
        if (msg != null) {
            if (Thread.currentThread() != Looper.getMainLooper().thread) {
                Log.i("AsyncTask", "toast: change")
                Handler(Looper.getMainLooper()).post {
                    toast(msg)
                }
                return
            }
            val context = activity ?: ActivityLifeManager.getInstance().getTopActivity()

            val runnable: (Activity?, String) -> Toast = { c, str ->
                val toast = if (c != null && !c.isFinishing) {
                    Toast.makeText(c, str, Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(getCurrentApplication(), str, Toast.LENGTH_SHORT)
                }
                toast
            }

            if (context is IHolderSupportActivityBusiness && cacheKey != null
                && context is LifecycleOwner && context.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
            ) {
                val mISaveStateHandle =
                    context.getHolderSupprotActivityBusiness().getLocalHolderSavedStateHandle()
                val cacheToast: Toast? = mISaveStateHandle[cacheKey]
                (cacheToast ?: runnable.invoke(context, msg).apply {
                    mISaveStateHandle[cacheKey] = this
                }).apply {
                    setText(msg)
                }.show()
            } else {
                runnable.invoke(context, msg).show()
            }
        }
    }

//    /**
//     * 保存基于“key”的value 存储于bundle基于SavedStateHandler api
//     */
//    @JvmStatic
//    fun <T> String.saveStateChange(holder: IHolderSavedStateHandle, v: T) {
//        holder.getHolderSavedStateHandle()[this] = v
//    }
//
//    /**
//     * 保存基于“key”的value 存储于bundle基于SavedStateHandler api
//     */
//    @JvmStatic
//    fun <T> String.saveStateChange(saveStateHandle: ISaveStateHandle, v: T) {
//        saveStateHandle[this] = v
//    }


//    @JvmStatic
//    fun <T> String.liveSaveStateObserver(
//        lifecycle: LifecycleOwner?,
//        saveStateHandle: IHolderSavedStateHandler?,
//        observer: Observer<T?>
//    ) {
//        liveSaveStateObserver(lifecycle, saveStateHandle?.getHolderSavedStateHandle(), observer)
//    }
//
//    @JvmStatic
//    fun <T> String.liveSaveStateObserver(
//        lifecycle: LifecycleOwner?, saveStateHandle: ISaveStateHandle?, observer: Observer<T?>
//    ) {
//        if (lifecycle != null) {
//            saveStateHandle?.getLiveData<T>(this)?.observe(lifecycle, observer)
//        } else {
//            saveStateHandle?.getLiveData<T>(this)?.observeForever(observer)
//        }
//    }


//    @JvmStatic
//    @JvmOverloads
//    fun <T> String.liveSaveStateObserverOnOwner(
//        holder: ISaveStateHandle,
//        myLifecycleOwner: LifecycleOwner?,
//        observer: Observer<T?>,
//        otherLifecycleOwner: LifecycleOwner,
//        isRemove: Boolean = false,
//    ) {
//        if (myLifecycleOwner != null) {
//            val liveData = holder.getLiveData<T>(this);
//            if (isRemove) {
//                liveData.observe(otherLifecycleOwner, object : Observer<T?> {
//                    override fun onChanged(t: T?) {
//                        observer.onChanged(t)
//                        liveData.removeObserver(this)
//                    }
//                })
//            } else {
//                liveData.observe(otherLifecycleOwner, observer)
//            }
//        }
//    }


//    /**
//     * 获取基于“key”的可观察的livedata
//     */
//    @JvmStatic
//    fun <T> String.getSaveStateLiveData(holder: IHolderSavedStateHandle): LiveData<T?> {
//        return holder.getHolderSavedStateHandle().getLiveData(this);
//    }
//
//    @JvmStatic
//    fun <T> String.getSaveStateLiveData(saveStateHandle: ISaveStateHandle): LiveData<T?> {
//        return saveStateHandle.getLiveData(this);
//    }

//    /**
//     * 获取基于“key”的可观察的value
//     */
//    @JvmStatic
//    fun <T> String.getSaveStateValue(holder: IHolderSavedStateHandle): T? {
//        return holder.getHolderSavedStateHandle()[this]
//    }
//
//    @JvmStatic
//    fun <T> String.getSaveStateValue(saveStateHandle: ISaveStateHandle): T? {
//        return saveStateHandle[this]
//    }

//    @JvmStatic
//    fun <T> String.savedStateAllLiveChangeValue(values: T) {
//        val r: (IHolderSavedStateHandle?, Activity) -> Unit = { f, activity ->
//            if (!activity.isFinishing) {
//                f?.getHolderSavedStateHandle()?.set(this, values)
//            }
//        }
//        for (activity in ActivityLifeManager.getInstance().getLifeActivityList()) {
//            if (activity is FragmentActivity) {
//                for (f in activity.supportFragmentManager.fragments) {
//                    r.invoke(f as? IHolderSavedStateHandle, activity)
//                }
//            }
//        }
//    }

    fun <N : IBusiness> Class<N>.getNextBusiness(next: INeed): N? {
        return next.getNext(this)
    }

    @JvmStatic
    fun INeed.doubleLimiter(k: String, run: Runnable) {
        getNext(DoubleLimiteBusiness::class.java)?.check(k, run)
    }

    @JvmStatic
    fun INeed.doubleLimiter(k: String, limiterTime: Int, run: Runnable) {
        getNext(DoubleLimiteBusiness::class.java)?.apply {
            limiter = limiterTime
            check(k, run)
        }
    }

    @JvmStatic
    fun EditText.search(call: (String) -> Unit) {
        setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
                        call(text.toString())
                        return true
                    }
                }
                return false
            }
        })
    }

    @JvmStatic
    fun EditText.searchByObservable(call: (Observable<String>) -> Unit) {
        setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
                        call(Observable.create { it.onNext(text.toString()) })
                        return true
                    }
                }
                return false
            }
        })
    }

    @JvmStatic
    fun Int.dp(): Float {
        return ScreenUtil.dp2px(this.toFloat()).toFloat()
    }

    @JvmStatic
    fun Int.getResUri(): Uri {
        return ApkUtil.getResUri(this)
    }

    @JvmStatic
    fun Int.getDrawable(): Drawable {
        return ActivityCompat.getDrawable(ActivityThreadHelp.getCurrentApplication(), this)!!
    }

    @JvmStatic
    fun Int.getResUriString(): String {
        return ApkUtil.getResUriString(this)
    }

    /**
     * viewpage 绑定一个adapter
     * @receiver Page
     * @param f FragmentManager
     * @param fragmentList List<Fragment>
     * @return Page
     */
    @JvmStatic
    fun <Page : ViewPager2> Page.bindAdapter(
        f: Fragment,
        fragmentList: List<Fragment>,
        offscreen: Int? = null
    ): Page {
        offscreenPageLimit = offscreen ?: fragmentList.size
        adapter = object : FragmentStateAdapter(f) {
            override fun getItemCount() = fragmentList.size

            override fun createFragment(position: Int) = fragmentList[position]

        }
        return this
    }

    /**
     * viewpage 绑定一个adapter
     * @receiver Page
     * @param f FragmentManager
     * @param fragmentList List<Fragment>
     * @return Page
     */
    @JvmStatic
    fun <Page : ViewPager2> Page.bindAdapter(
        fragmentManager: FragmentManager,
        lifecycle: LifecycleOwner,
        fragmentList: List<Fragment>,
        offscreen: Int? = null
    ): Page {
        offscreenPageLimit = offscreen ?: fragmentList.size
        adapter = object : FragmentStateAdapter(fragmentManager, lifecycle.lifecycle) {
            override fun getItemCount() = fragmentList.size

            override fun createFragment(position: Int) = fragmentList[position]

        }
        return this
    }


}