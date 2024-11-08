package com.musongzi.core.base.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.musongzi.core.ExtensionCoreMethod.layoutInflater
import com.musongzi.core.base.client.IFragmentControlClient
import com.musongzi.core.base.client.imp.FragmentBusinessControlClientImpl
import com.musongzi.core.itf.holder.IHolderActivity
import com.musongzi.core.itf.holder.IHolderLayoutInflater
import com.musongzi.core.itf.holder.IHolderUiTheme
import com.musongzi.core.itf.holder.IHolderViewModelFactory
import com.musongzi.core.itf.holder.IHolderViewModelProvider
import com.musongzi.core.util.InjectionHelp
import com.musongzi.core.view.TipDialog
import com.trello.rxlifecycle4.components.support.RxFragment

/**
create by linhui , data = 2023/7/1 0:03
 **/
abstract class BaseLayoutFragment : RxFragment(), IHolderActivity, IFragmentControlClient, IHolderUiTheme, IHolderLayoutInflater {
    private lateinit var fControl: IFragmentControlClient
    protected val TAG: String = javaClass.name
    private var tipDialog: Dialog? = null

    override var holderUiThemeRes: Int = 0

    override fun showDialog(msg: String?) {
        (tipDialog ?: let {
            val t = createDialog()
            tipDialog = t;
            t
        }).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return createView(getHolderLayoutInflater() ?: inflater, container, savedInstanceState)
    }

    override fun getHolderLayoutInflater(): LayoutInflater? =
        if (holderUiThemeRes == 0) {
            layoutInflater
        } else {
            LayoutInflater.from(ContextThemeWrapper(requireContext(), holderUiThemeRes))
        }


    protected open fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getLayoutId().layoutInflater(inflater, container)
    }

//    override fun onClearOperate(any: Any?): Boolean {
//        return true
//    }

    protected abstract fun getLayoutId(): Int


    override fun disimissDialog() {
        tipDialog?.apply {
            dismiss()
        }
    }

    override fun disconnect(): Boolean {
        return if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && activity != null) {
            requireActivity().finish()
            true
        } else {
            false
        }
    }

    override fun fragmentControlLayoutId(): Int = 0

    override fun getHolderFragmentManager() = childFragmentManager

    override fun getHolderParentFragmentManager(): FragmentManager? = parentFragmentManager

//    override fun getNextByClass(nextClass: Class<*>): IClient?  = nul

    override fun getHolderActivity(): FragmentActivity? = activity

    override fun getMainLifecycle(): IHolderActivity? = requireActivity() as? IHolderActivity

    override fun getThisLifecycle(): LifecycleOwner? = this

    override fun getHolderContext(): Context? = context

    override fun putArguments(d: Bundle?) {
        arguments = d;
    }


    private fun getFactoryDefaultArgs(): Bundle? {
        return arguments;
    }

    override fun topViewModelProvider(): ViewModelProvider? {
        return if (requireActivity() is IHolderViewModelProvider) {
            (requireActivity() as IHolderViewModelProvider).topViewModelProvider()
        } else {
            val factory = requireActivity() as? IHolderViewModelFactory
            ViewModelProvider(
                requireActivity(),
                (factory?.getHolderFactory() ?: newFactory(requireActivity()))
            )
        }
    }

    override fun thisViewModelProvider(): ViewModelProvider? {
        return ViewModelProvider(this, defaultViewModelProviderFactory)
    }

    private var factory: ViewModelProvider.Factory? = null

    protected fun newFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory =
        safeGetFactory(owner)

    private fun safeGetFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return factory ?: object :
            AbstractSavedStateViewModelFactory(owner, getFactoryDefaultArgs()) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return InjectionHelp.injectViewModel(
                    this@BaseLayoutFragment,
                    arguments,
                    modelClass,
                    handle
                )!!
            }
        }.apply {
            factory = this
        }
    }


    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return newFactory(this)
    }

    protected open fun createDialog() = TipDialog(requireActivity())

    override fun runOnUiThread(runnable: Runnable) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            requireActivity().runOnUiThread(runnable)
        }
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData();
//        initEvent()
    }

    override fun addFragment(fragment: Fragment, tag: String?, isHide: Boolean) {
        fControl.addFragment(fragment, tag, isHide)
    }

    override fun <F : Fragment> addFragment(
        fragmentClass: Class<F>,
        tag: String?,
        isHide: Boolean
    ) {
        fControl.addFragment(fragmentClass, tag, isHide)
    }

    override fun replaceFragment(fragment: Fragment, tag: String?, isHide: Boolean) {
        fControl.replaceFragment(fragment, tag, isHide)
    }

    override fun <F : Fragment> replaceFragment(
        fragmentClass: Class<F>,
        tag: String?,
        isHide: Boolean
    ) {
        fControl.replaceFragment(fragmentClass, tag, isHide)
    }

    override fun removeFragment(tag: String) {
        fControl.removeFragment(tag)
    }

    override fun removeFragment(fragment: Fragment) {
        fControl.removeFragment(fragment)
    }

    override fun <F : Fragment> removeFragment(fragmentClass: Class<F>) {
        fControl.removeFragment(fragmentClass)
    }

    override fun getFragmentByTag(tag: String): Fragment? = fControl.getFragmentByTag(tag)


    abstract fun initView()

    //    abstract fun initEvent()
    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fControl = FragmentBusinessControlClientImpl(this)
        Log.d(TAG, "FragmentState:onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "FragmentState:onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "FragmentState:onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "FragmentState:onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "FragmentState:onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "FragmentState:onDestory")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "FragmentState:onDestoryView")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "FragmentState:onDetach")
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Log.d(TAG, "FragmentState:onAttach(activity:$activity)")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "FragmentState:onAttach(context:$context)")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "FragmentState:onHiddenChange($hidden)")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d(TAG, "FragmentState:onLowMemory")
    }

}