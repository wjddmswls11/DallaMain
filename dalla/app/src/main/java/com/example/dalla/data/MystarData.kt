package com.example.dalla.data

import com.google.gson.annotations.SerializedName

data class MyStar(
    val nickNm : String,
    val title : String,
    val profImg : ProfImg
)

data class ProfImg(
    val url: String,
    val path: String
)

data class MyStarResponse(
    @SerializedName("StarList") val starList: ArrayList<MyStar>
)