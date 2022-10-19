package com.musongzi.test.fragment

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.musongzi.CONFIG_MANAGER
import com.musongzi.ConfigManager
import com.musongzi.comment.viewmodel.EsayApiViewModel
import com.musongzi.core.ExtensionCoreMethod.adapter
import com.musongzi.core.ExtensionCoreMethod.getApi
import com.musongzi.core.ExtensionCoreMethod.sub
import com.musongzi.core.ExtensionCoreMethod.toJson
import com.musongzi.core.StringChooseBean
import com.musongzi.core.base.business.collection.IHolderCollections
import com.musongzi.core.base.client.IRecycleViewClient
import com.musongzi.core.base.client.IRefreshClient
import com.musongzi.core.base.client.IRefreshViewClient
import com.musongzi.core.base.fragment.BaseCollectionsViewFragment
import com.musongzi.core.base.fragment.CollectionsViewFragment
import com.musongzi.core.base.fragment.MszFragment
import com.musongzi.core.base.fragment.QuickCollectionFragment
import com.musongzi.core.base.manager.ManagerUtil.manager
import com.musongzi.core.base.vm.EsayViewModel
import com.musongzi.core.itf.page.IPageEngine
import com.musongzi.test.Api
import com.musongzi.test.MszTestApi
import com.musongzi.test.bean.ResponeCodeBean
import com.musongzi.test.databinding.AdapterStringBinding
import com.musongzi.test.databinding.FragmentSoulAppTestBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.rxjava3.core.Observable

/*** created by linhui * on 2022/10/17 */
class SoulAppTestFragemnt : QuickCollectionFragment<FragmentSoulAppTestBinding, StringChooseBean, ResponeCodeBean<List<StringChooseBean>>>() {


    override fun createRecycleViewClient(): IRefreshViewClient {
        return object : IRefreshViewClient {
            override fun normalView(): View? {
                return null
            }

            override fun recycleView(): RecyclerView? {
                return dataBinding.idRecyclerView
            }

            override fun refreshView(): SmartRefreshLayout? {
                return dataBinding.idSmartRefreshLayout
            }

            override fun emptyView(): ViewGroup? {
                return null
            }

        }
    }

    override fun transformDataToList(entity: ResponeCodeBean<List<StringChooseBean>>): List<StringChooseBean> {
        return entity.data
    }

    override fun getRemoteData(index: Int): Observable<ResponeCodeBean<List<StringChooseBean>>>? =
        MszTestApi::class.java.getApi(this)?.getArrayEngine(index, getPageEngine()?.pageSize())


    override fun getAdapter(page: IPageEngine<StringChooseBean>?): RecyclerView.Adapter<*>? {
        return page?.adapter(AdapterStringBinding::class.java)
    }


}