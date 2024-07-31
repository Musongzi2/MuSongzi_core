package com.musongzi.music.impl

import androidx.lifecycle.LifecycleOwner
import com.musongzi.core.base.page2.PageCallBack
import com.musongzi.core.base.page2.RequestObservableBean
import com.musongzi.core.itf.IAttribute
import com.musongzi.core.itf.page.IAdMessage
import com.musongzi.core.itf.page.IPageEngine
import com.musongzi.music.itf.RemoteDataPacket
import io.reactivex.rxjava3.core.Observable

/*** created by linhui * on 2022/8/3 */
class MusicPageCallBack<I : IAttribute, D>(var packet: RemoteDataPacket<I, D>) : PageCallBack<I, D> {

    override fun pageSize() = IPageEngine.PAGE_SIZE
    override fun getAdMessage(): IAdMessage<I>? = null

    override val thisLifecycle: LifecycleOwner? = null

    override fun handlerState(state: Int?) {

    }

    override fun getBusinessMode(): Int {
        TODO("Not yet implemented")
    }

    override fun getRemoteData(page: Int, pageSize: Int): Observable<D>? {
        TODO("Not yet implemented")
    }

    override fun createPostEvent(request: RequestObservableBean<D>): Any? {
        TODO("Not yet implemented")
    }

    override fun handlerDataChange(data: MutableList<I>, request: RequestObservableBean<D>) {
        TODO("Not yet implemented")
    }

    override fun thisStartPage() = 0
    override fun convertListByNewData(data: MutableList<I>, transList: MutableList<I>) {
        TODO("Not yet implemented")
    }

    override fun transformDataToList(entity: D?): MutableList<I> {
        TODO("Not yet implemented")
    }


}