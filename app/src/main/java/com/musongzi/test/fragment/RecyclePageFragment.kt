package com.musongzi.test.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.musongzi.core.ExtensionCoreMethod.liveSaveStateObserver
import com.musongzi.core.base.fragment.DataBindingFragment
import com.musongzi.core.base.fragment.MszFragment
import com.musongzi.test.MszTestApi
import com.musongzi.test.R
import com.musongzi.test.bean.UserInfo
import com.musongzi.test.databinding.FragmentRecyclePageBinding
import com.musongzi.test.vm.ListDataViewModel

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


    val viewmodel: ListDataViewModel by viewModels()

    override fun initView() {

        viewmodel.loadDataUser()

        "hahakey".liveSaveStateObserver<String>(this, viewmodel) {
            Log.i(TAG, "ListDataViewModel liveSaveStateObserver: hahakey = $it")
            dataBinding.idTitle.text = it
        }

    }

    override fun initData() {


        ListDataViewModel.DATA.liveSaveStateObserver<MutableList<UserInfo>>(
            this,
            viewmodel.localSavedStateHandle
        ) {
            Log.i(TAG, "ListDataViewModel initData: ${it?.get(1).toString()}")
        }

    }
}