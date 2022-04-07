package com.musongzi.test

import com.musongzi.test.bean.DiscoverBannerBean
import com.musongzi.test.bean.ListDataBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface Api {

    @GET("banner/bannerList")
    fun getBannerList(): Observable<ListDataBean<DiscoverBannerBean>>

    fun getArrayEngine(): Observable<List<String>>

}