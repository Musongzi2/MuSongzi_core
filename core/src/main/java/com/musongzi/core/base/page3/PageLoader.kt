package com.musongzi.core.base.page3

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.musongzi.core.itf.INotifyDataSetChanged
import com.musongzi.core.itf.page.Book
import com.musongzi.core.itf.page.ICheckDataEnd
import com.musongzi.core.itf.page.IRead
import com.musongzi.core.itf.page.ISource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
create by linhui , data = 2024/8/25 0:30
 **/
class PageLoader<T> private constructor(private var page: Int = START_PAGE, private var pageSize: Int = MAX_SIZE, val call: (page: Int, pageSize: Int) -> Flow<List<T>>) : Book, IRead,
    ISource<T> {

    private var lifecycleOwner: LifecycleOwner? = null
    private var isEnd = false
    private var checkData: ICheckDataEnd? = null
    private val datas by lazy {
        mutableListOf<T>()
    }
    private val onDataChanges by lazy {
        mutableListOf<(List<T>) -> Unit>()
    }
    private var setDataTransformations: ((MutableList<T>, List<T>) -> Unit)? = null
    private val dispatchOnNexts by lazy {
        mutableListOf<(Int, Int) -> Unit>().apply {
            add(refreshCall)
        }
    }
    private val dispatchOnRefreshs by lazy {
        mutableListOf<(Int, Int) -> Unit>().apply {
            add(nextCall)
        }
    }
    private val refreshCall = { page: Int, pageSize: Int ->
        refreshInner(page, pageSize)
    }
    private val nextCall = { page: Int, pageSize: Int ->
        nextInner(page, pageSize)
    }

    class Build<T>(private var call: (page: Int, pageSize: Int) -> Flow<List<T>>) {
        private var lifecycleOwner: LifecycleOwner? = null
        private var page: Int = START_PAGE
        private var pageSize: Int = MAX_SIZE

        private var dataChange: ((List<T>) -> Unit)? = null
        private var onRefresh: ((Int, Int) -> Unit)? = null
        private var onNext: ((Int, Int) -> Unit)? = null
        private var setDataTransformations: ((MutableList<T>, List<T>) -> Unit)? = null

        fun dataTransformations(d: (MutableList<T>, List<T>) -> Unit): Build<T> {
            setDataTransformations = d
            return this
        }

        fun dataChange(d: (List<T>) -> Unit): Build<T> {
            dataChange = d
            return this
        }

        fun onRefresh(on: (page: Int, size: Int) -> Unit): Build<T> {
            onRefresh = on
            return this
        }

        fun onNext(on: (page: Int, size: Int) -> Unit): Build<T> {
            onNext = on
            return this
        }

        fun build(): IRead {
            return PageLoader(page, pageSize, call).apply {
                dataChange?.apply {
                    addDataChange(lifecycleOwner, this)
                }
                onRefresh?.apply {
                    addDispatchOnRefresh(lifecycleOwner, refreshCall)
                }
                onNext?.apply {
                    addDispatchOnNext(lifecycleOwner, this)
                }
                this.lifecycleOwner = this@Build.lifecycleOwner
                this.setDataTransformations = this@Build.setDataTransformations
            }
        }

    }

    companion object {
        const val MAX_SIZE = 20
        const val START_PAGE = 0
    }

    override fun pageSize() = pageSize

    override fun thisStartPage() = START_PAGE

    private fun refreshInner(page: Int, pageSize: Int) {
        loadData(page, pageSize)
    }


    private fun nextInner(page: Int, pageSize: Int) {
        loadData(page, pageSize)
    }

    private fun loadData(page: Int, pageSize: Int) {
        val scope: CoroutineScope = if (lifecycleOwner == null) {
            GlobalScope
        } else {
            lifecycleOwner!!.lifecycleScope
        }
        scope.launch(Dispatchers.IO) {
            call(page, pageSize).collect {
                withContext(Dispatchers.Main) {
                    if (setDataTransformations == null) {
                        datas.addAll(it)
                    } else {
                        setDataTransformations?.invoke(datas, it)
                    }
                    isEnd = checkData?.checkDataIsNull(datas) ?: false
                    dispatchOnDataChange(datas)
                }
            }
        }
    }


    fun addDataChange(lifecycleOwner: LifecycleOwner?, on: (List<T>) -> Unit) {
        if (lifecycleOwner != null) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    onDataChanges.add(on)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    onDataChanges.add(on)
                }
            })
        } else {
            onDataChanges.add(on)
        }
    }

    fun removeDataChange(on: (List<T>) -> Unit) {
        onDataChanges.remove(on)
    }

    fun dispatchOnDataChange(data: List<T>) {
        for (on in onDataChanges) {
            on(data)
        }
    }


    fun addDispatchOnNext(lifecycleOwner: LifecycleOwner?, on: (Int, Int) -> Unit) {
        if (lifecycleOwner != null) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    dispatchOnNexts.add(on)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    dispatchOnNexts.add(on)
                }
            })
        } else {
            dispatchOnNexts.add(on)
        }

    }

    fun addDispatchOnRefresh(lifecycleOwner: LifecycleOwner?, on: (Int, Int) -> Unit) {
        if (lifecycleOwner != null) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    dispatchOnRefreshs.add(on)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    dispatchOnRefreshs.remove(on)
                }
            })
        } else {
            dispatchOnRefreshs.add(on)
        }
    }

    fun removeDispatchOnNext(on: (Int, Int) -> Unit) {
        dispatchOnNexts.remove(on)
    }

    fun removeDispatchOnRefresh(on: (Int, Int) -> Unit) {
        dispatchOnRefreshs.remove(on)
    }

    override fun refresh() {
        page = thisStartPage()
        dispatchOnRefresh(page)
    }

    override fun next() {
        if (isEnd()) {
            return
        }
        page++
        dispatchOnNext(page)
    }

    private fun dispatchOnRefresh(page: Int) {
        for (l in dispatchOnRefreshs) {
            l(page, pageSize)
        }
    }

    private fun dispatchOnNext(page: Int) {
        for (l in dispatchOnNexts) {
            l(page, pageSize)
        }
    }

    override fun isEnd() = isEnd
    override fun realData(): List<T> {
        return datas
    }


}