package androidx.lifecycle

import android.util.Log
import com.musongzi.core.base.map.LocalSavedHandler
import com.musongzi.core.itf.*
import com.musongzi.core.itf.client.IContextClient
import com.musongzi.core.itf.holder.IHolderLocaSavedStateHandler
import com.musongzi.core.util.UiUtil
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.RxLifecycle
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.io.Closeable
import java.lang.ref.WeakReference

abstract class CoreViewModel : ViewModel(), IAttach<IContextClient>, IWant, IWant2, IDisconnect, IHolderLocaSavedStateHandler {
    protected val TAG = javaClass.simpleName

    companion object {
        const val LOCAL_SAVED_INDEX = 0;
        const val REMOTE_SAVED_INDEX = 1;
        const val SAVEDS_MAX = LOCAL_SAVED_INDEX + REMOTE_SAVED_INDEX + 1
    }

    override fun runOnUiThread(runnable: Runnable) {
        UiUtil.post(runnable = runnable)
    }

    private val lifecycleSubject = BehaviorSubject.create<ViewModelEvent>()

    private val mLifecycleProvider = object : LifecycleProvider<ViewModelEvent> {
        override fun lifecycle() = lifecycleSubject.hide()

        override fun <T : Any?> bindUntilEvent(event: ViewModelEvent): LifecycleTransformer<T> {
            return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
        }

        override fun <T : Any?> bindToLifecycle() = this@CoreViewModel.bindToLifecycle<T>()

    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycle.bind(lifecycleSubject) {
            Log.d(TAG, "loadData bindToLifecycle: $it")
            when (it) {
                ViewModelEvent.LIFE -> {
                    ViewModelEvent.DIE
                }

                else -> {
                    TODO("error ")
                }
            }
        }
    }


    init {
        lifecycleSubject.onNext(ViewModelEvent.LIFE)
    }

    override fun addOnClose(close: Closeable) {
        setTagIfAbsent(close.hashCode().toString(), close)
//        val method = ViewModel::class.java.getDeclaredMethod("setTagIfAbsent", String::class.java, Object::class.java)
//        method.isAccessible = true
//        method.invoke(this, close.hashCode().toString(), close)
    }

    /**
     * 有可能为空，如果是默认factory 注入的话
     * 不会走attachNow（）
     */
    @Deprecated("过期，不安全")
    protected var holderActivity: WeakReference<IContextClient?>? = null

    //    private val coLifeCycleImpl by lazy {
//        VmCoLifeCycle()
//    }
    protected var mSavedStateHandles = arrayOfNulls<ISaveStateHandle?>(SAVEDS_MAX)

    override val localSavedStateHandle: ISaveStateHandle
        get() = mSavedStateHandles[LOCAL_SAVED_INDEX] ?: LocalSavedHandler().apply { mSavedStateHandles[LOCAL_SAVED_INDEX] = this }

//    override fun dispoasble(disposable: Disposable?) {
//        coLifeCycleImpl.dispoasble(disposable)
//    }

    override fun onCleared() {
        holderActivity = null
        lifecycleSubject.onNext(ViewModelEvent.DIE)
        Log.d(TAG, "onCleared: loadDataUser viewmodel 已死")
    }

//    override fun onClearOperate(any: Any?) = true

    @Deprecated("")
    override fun attachNow(t: IContextClient?) {
        Log.i(TAG, "attachNow: = $t")
        t?.apply {
            holderActivity = WeakReference(this)
        }
    }

    final override fun disconnect(): Boolean {
        return holderActivity?.get()?.disconnect() ?: true
    }

    final override fun isAttachNow(): Boolean = holderActivity != null


    enum class ViewModelEvent {

        LIFE,
        DIE
    }

}