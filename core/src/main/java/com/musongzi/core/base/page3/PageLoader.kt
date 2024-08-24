package com.musongzi.core.base.page3

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.musongzi.core.itf.page.Book
import com.musongzi.core.itf.page.ICheckDataEnd
import com.musongzi.core.itf.page.IRead
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
create by linhui , data = 2024/8/25 0:30
 **/
class PageLoader<T> private constructor(private var page: Int = START_PAGE, private var pageSize: Int = MAX_SIZE, val call: (page: Int, pageSize: Int) -> Flow<T>) : Book, IRead {

    class Build<T>(private var call: (page: Int, pageSize: Int) -> Flow<T>) {
        private var lifecycleOwner: LifecycleOwner? = null
        private var page: Int = START_PAGE
        private var pageSize: Int = MAX_SIZE

        private var dataChange: ((List<T>) -> Unit)? = null
        private var onRefresh: ((Int, Int) -> Unit)? = null
        private var onNext: ((Int, Int) -> Unit)? = null

        fun dataChange(d: (List<T>) -> Unit): Build<T> {
            dataChange = d
            return this
        }

        fun onRefresh(on: (page:Int, size:Int) -> Unit): Build<T> {
            onRefresh = on
            return this
        }

        fun onNext(on: (page:Int, size:Int) -> Unit): Build<T> {
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
            }
        }

    }

    companion object {
        const val MAX_SIZE = 20
        const val START_PAGE = 0
    }

    private var isEnd = false

    override fun pageSize() = pageSize

    override fun thisStartPage() = START_PAGE

    private var checkData: ICheckDataEnd? = null

    private val refreshCall = { page: Int, pageSize: Int ->
        refreshInner()
    }

    private val nextCall = { page: Int, pageSize: Int ->
        nextInner()
    }

    private fun refreshInner() {

    }

    private fun nextInner() {

    }

    private val onDataChanges = mutableListOf<(List<T>) -> Unit>()

    private val dispatchOnNext = mutableListOf<(Int, Int) -> Unit>().apply {
        add(refreshCall)
    }
    private val dispatchOnRefresh = mutableListOf<(Int, Int) -> Unit>().apply {
        add(nextCall)
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

    fun addDispatchOnNext(lifecycleOwner: LifecycleOwner?, on: (Int, Int) -> Unit) {
        if (lifecycleOwner != null) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    dispatchOnNext.add(on)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    dispatchOnNext.add(on)
                }
            })
        } else {
            dispatchOnNext.add(on)
        }

    }

    fun addDispatchOnRefresh(lifecycleOwner: LifecycleOwner?, on: (Int, Int) -> Unit) {
        if (lifecycleOwner != null) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    dispatchOnRefresh.add(on)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    dispatchOnRefresh.remove(on)
                }
            })
        } else {
            dispatchOnRefresh.add(on)
        }
    }

    fun removeDispatchOnNext(on: (Int, Int) -> Unit) {
        dispatchOnNext.remove(on)
    }

    fun removeDispatchOnRefresh(on: (Int, Int) -> Unit) {
        dispatchOnRefresh.remove(on)
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

    }

    private fun dispatchOnNext(page: Int) {

    }

    override fun isEnd() = isEnd


}