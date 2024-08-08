package com.musongzi.core.base.client

import androidx.fragment.app.Fragment

interface IFragmentControlClient : IFragmentClient {

    fun addFragment(fragment: Fragment, tag: String? = null, isHide: Boolean = false)
    fun <F : Fragment> addFragment(fragmentClass: Class<F>, tag: String? = null, isHide: Boolean = false)

    fun replaceFragment(fragment: Fragment, tag: String? = null, isHide: Boolean = false)
    fun <F : Fragment> replaceFragment(fragmentClass: Class<F>, tag: String? = null, isHide: Boolean = false)

    fun removeFragment(tag: String)
    fun removeFragment(fragment: Fragment)
    fun <F : Fragment> removeFragment(fragmentClass: Class<F>)

    fun getFragmentByTag(tag: String) : Fragment?

}