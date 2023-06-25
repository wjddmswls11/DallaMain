package com.example.dalla.retrofit

import com.example.dalla.data.BannerResponse
import com.example.dalla.data.EventResponse
import com.example.dalla.data.MyStarResponse
import com.example.dalla.data.PageNoData
import com.example.dalla.data.RoomResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface DallaRetrofit {
    @GET("/RqBannerList")
    suspend fun getRetrofitData(): BannerResponse
    @GET("/RqMyStarList")
    suspend fun getMyStarData(): MyStarResponse

    @GET("/RqEventBannerList")
    suspend fun getEventData(): EventResponse

    @POST("/RqRoomList")
    suspend fun postRoomList(@Body requestBody: PageNoData): RoomResponse
}