package com.example.dalla.data

import com.google.gson.annotations.SerializedName

//@SerializedName은 키값과 변수명이 다를 떄 사용
//List는 해당 타입을 명확히 알기 위해 @SerializedName을 사용해야 함

data class Banner(
   val title: String,
   @SerializedName("image_profile") val imageProfile: String,
   @SerializedName("mem_nick") val memNick: String,
   val badgeSpecial: Int
)

data class BannerResponse(
   @SerializedName("BannerList") val bannerList: ArrayList<Banner>
)