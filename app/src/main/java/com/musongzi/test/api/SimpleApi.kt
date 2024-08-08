package com.musongzi.test.api

import com.musongzi.core.StringChooseBean
import com.musongzi.core.base.manager.RetrofitManager.CallBack
import com.musongzi.test.bean.ResponeCodeBean
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface SimpleApi {

    @FormUrlEncoded
    @POST("testArray")
    suspend fun getArrayEngine(
        @Field("page") page: Int,
        @Field("size") size: Int?,
        @Field("lastId") lastId: String? = null
    ): ResponeCodeBean<List<StringChooseBean>>

    @Streaming
    @GET
    fun getMusic(@Url str:String = "http://192.168.1.106:8080/我的刻苦铭心的恋人.mp3"):Observable<ResponseBody>


}