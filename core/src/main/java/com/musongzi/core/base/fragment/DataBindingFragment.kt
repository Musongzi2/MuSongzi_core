package com.musongzi.core.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.ViewDataBinding
import com.musongzi.core.itf.IDisconnect
import com.musongzi.core.itf.holder.*
import com.musongzi.core.util.InjectionHelp

abstract class DataBindingFragment<D : ViewDataBinding> : BaseLayoutFragment(),
    IDisconnect, IHolderDataBinding<D> {


    protected lateinit var dataBinding: D


    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "FragmentState:onCreateView")
        val v = instanceView(layoutInflater, container!!)
        dataBinding.lifecycleOwner = this
        return v
    }

    override fun getLayoutId() = 0

    private fun instanceView(inflater: LayoutInflater, container: ViewGroup): View? {

        Log.i(TAG, "FragmentState:instanceView findDataBinding")
        if (view == null) {
            dataBinding = InjectionHelp.findDataBinding(
                inflater,
                javaClass,
                container,
                superDatabindingName(),
                actualTypeArgumentsDatabindinIndex(),createDataBindingCompact()
            )!!
        }
        return dataBinding.root
    }

    private fun createDataBindingCompact(): DataBindingComponent? {
        return null
    }


    protected open fun superDatabindingName(): String = DataBindingFragment::class.java.name

    protected open fun actualTypeArgumentsDatabindinIndex(): Int = 0

    override fun getHolderDataBinding(): D = dataBinding

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding.unbind()
        Log.i(TAG, "FragmentState:onDestoryView")
    }

}