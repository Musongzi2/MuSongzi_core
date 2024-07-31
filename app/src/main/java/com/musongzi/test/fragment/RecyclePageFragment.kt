package com.musongzi.test.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.musongzi.comment.util.SourceImpl
import com.musongzi.core.ExtensionCoreMethod.adapter
import com.musongzi.core.ExtensionCoreMethod.linearLayoutManager
import com.musongzi.core.ExtensionCoreMethod.liveSaveStateObserver
import com.musongzi.core.ExtensionCoreMethod.refreshLayoutInit
import com.musongzi.core.ExtensionCoreMethod.saveStateChange
import com.musongzi.core.base.bean.IUserInfo
import com.musongzi.core.base.fragment.DataBindingFragment
import com.musongzi.core.base.fragment.MszFragment
import com.musongzi.core.base.page2.PageLoader
import com.musongzi.core.base.page2.RequestObservableBean
import com.musongzi.core.base.page2.SimplePageCall
import com.musongzi.core.itf.page.IPageEngine
import com.musongzi.test.MszTestApi
import com.musongzi.test.R
import com.musongzi.test.bean.ListDataBean
import com.musongzi.test.bean.UserInfo
import com.musongzi.test.databinding.FragmentRecyclePageBinding
import com.musongzi.test.databinding.ItemUserInfoBinding
import com.musongzi.test.vm.ListDataViewModel
import io.reactivex.rxjava3.core.Observable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecyclePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecyclePageFragment : DataBindingFragment<FragmentRecyclePageBinding>() {

    private var pageLoad: IPageEngine<UserInfo>? = null
    val viewmodel: ListDataViewModel by viewModels()

    override fun initView() {


        "hahakey".liveSaveStateObserver<String>(this, viewmodel) {
            Log.i(TAG, "ListDataViewModel liveSaveStateObserver: hahakey = $it")
            dataBinding.idTitle.text = it
        }

        dataBinding.idSmartRefreshLayout.refreshLayoutInit(safeGetPageEngine())

        if (dataBinding.idRecyclerView.adapter == null) {
            dataBinding.idRecyclerView.linearLayoutManager {
                safeGetPageEngine().adapter(ItemUserInfoBinding::class.java)
            }
        }
    }

    private fun safeGetPageEngine(): IPageEngine<UserInfo> {
        if (pageLoad == null) {
            pageLoad = PageLoader.createInstance(object :
                SimplePageCall<UserInfo>(this@RecyclePageFragment) {
                override fun getRemoteData(page: Int, pageSize: Int): Observable<List<UserInfo>>? {
                    Log.i(TAG, "getRemoteData: page = $page , pageSize = $pageSize")
                    return Observable.create {
                        val dataBean = ListDataBean<UserInfo>()
                        it.onNext(dataBean.data.apply {


                            if(page == 0) {
                                add(UserInfo(System.currentTimeMillis(), "小米"))
                                add(UserInfo(System.currentTimeMillis(), "小明"))
                                add(UserInfo(System.currentTimeMillis(), "小白"))
                                add(UserInfo(System.currentTimeMillis(), "小红", gender = IUserInfo.WOMAN))
                                add(UserInfo(System.currentTimeMillis(), "小寻", gender = IUserInfo.WOMAN))
                                add(UserInfo(System.currentTimeMillis(), "小志"))
                                add(UserInfo(System.currentTimeMillis(), "小薰", gender = IUserInfo.WOMAN))
                                add(UserInfo(System.currentTimeMillis(), "小强"))
                                add(UserInfo(System.currentTimeMillis(), "小赞"))
                                add(UserInfo(System.currentTimeMillis(), "小芸", gender = IUserInfo.WOMAN))
                            }else if(page == 1) {
                                add(UserInfo(System.currentTimeMillis(), "小欧"))
                                add(UserInfo(System.currentTimeMillis(), "小林"))
                                add(UserInfo(System.currentTimeMillis(), "小晨", gender = IUserInfo.WOMAN))
                                add(UserInfo(System.currentTimeMillis(), "小婷", gender = IUserInfo.WOMAN))
                                add(UserInfo(System.currentTimeMillis(), "小琪", gender = IUserInfo.WOMAN))
                                add(UserInfo(System.currentTimeMillis(), "小国"))
                                add(UserInfo(System.currentTimeMillis(), "小海"))
                                add(UserInfo(System.currentTimeMillis(), "小痞"))
                            }
                        })
                        it.onComplete()
                    }
                }

                override fun pageSize(): Int = 10

                override fun handlerState(state: Int?) {
                    super.handlerState(state)
                    Log.i(TAG, "handlerState: $state")
                }

                override fun handlerDataChange(
                    data: MutableList<UserInfo>,
                    request: RequestObservableBean<List<UserInfo>>
                ) {
                    Log.i(TAG, "handlerDataChange: data.size = ${data.size}")
                    dataBinding.idRecyclerView.adapter?.notifyDataSetChanged()
                }
            }).apply {
//                setCheckDataEnd {
//                    (it?.size ?: 0) > 0
//                }
            }
        }
        return pageLoad!!
    }

    override fun initData() {
//        safeGetPageEngine().refresh()
    }
}