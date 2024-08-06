package com.musongzi.test.api

import retrofit2.http.Url

interface SimpleApi {



    fun getMusic(@Url str:String = "http://192.168.1.106:8080/我的刻苦铭心的恋人.mp3")


}