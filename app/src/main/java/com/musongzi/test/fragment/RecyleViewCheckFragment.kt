package com.musongzi.test.fragment

import android.util.Log
import android.view.View
import com.musongzi.comment.util.SourceImpl
import com.musongzi.core.ExtensionCoreMethod.adapter
import com.musongzi.core.ExtensionCoreMethod.linearLayoutManager
import com.musongzi.core.ExtensionCoreMethod.sub
import com.musongzi.core.StringChooseBean
import com.musongzi.core.base.fragment.DataBindingFragment
import com.musongzi.core.base.manager.RetrofitManager
import com.musongzi.core.util.ScreenUtil
import com.musongzi.test.MszTestApi
import com.musongzi.test.databinding.AdapterStringBinding
import com.musongzi.test.databinding.FragmentRecycleCheckBinding

/*** created by linhui * on 2022/9/22 */
class RecyleViewCheckFragment : DataBindingFragment<FragmentRecycleCheckBinding>() {

    var sum = 0;
    var source = SourceImpl<StringChooseBean>()

    override fun initView() {


        var height = dataBinding.idNestedScrollView.layoutParams.height
        val width = dataBinding.idNestedScrollView.layoutParams.width
        height = View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.getSize(height),
            View.MeasureSpec.UNSPECIFIED
        );
        dataBinding.idNestedScrollView.measure(width, height)
        dataBinding.idRecyclerView.layoutParams.height =
            ScreenUtil.getScreenHeight() - ScreenUtil.dp2px(400f)

//        dataBinding.idRecyclerView.head

        Log.i(
            TAG,
            "initView: AdapterString idNestedScrollView height = ${dataBinding.idNestedScrollView.measuredHeight}," +
                    "sceen height = ${ScreenUtil.getScreenHeight()}"
        )

        RetrofitManager.getInstance().getApi(MszTestApi::class.java).getArrayEngine(page = 0,20).sub {
            source.realData().addAll(it.data?: mutableListOf())
            dataBinding.idRecyclerView.linearLayoutManager {
                source.adapter(AdapterStringBinding::class.java, { d, t ->
                    Log.i(TAG, "AdapterString: convert sum = ${++sum}")
                }) { _, _, p ->
                    Log.i(TAG, "AdapterString: position = $p")
                }
            }
        }

    }



    override fun initData() {

    }


}