package com.musongzi.test.api

import com.musongzi.core.base.manager.RetrofitManager.CallBack
import io.reactivex.rxjava3.core.Observable
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface SimpleApi {



    @Streaming
    @GET
    fun getMusic(@Url str:String = "http://192.168.1.106:8080/我的刻苦铭心的恋人.mp3"):Observable<ResponseBody>


}