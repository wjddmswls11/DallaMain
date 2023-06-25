package com.example.dalla.data

import com.google.gson.annotations.SerializedName

data class Event(
    val bannerUrl: String
)

data class EventResponse(
    @SerializedName("BannerList") val eventBannerList: ArrayList<Event>
)