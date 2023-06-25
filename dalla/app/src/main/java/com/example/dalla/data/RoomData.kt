package com.example.dalla.data

import com.google.gson.annotations.SerializedName

data class Room(
    val roomTitle: String,
    val thumbnailUrl : String,
    val teamMedalUrl : String,
    val bjNickName : String,
    val bjMemSex : String,
    val teamBgUrl : String,
    val typeMedia : String,
    val countGood : Int,
    val countByeol : Int,
    val teamBgCode : String,
    val subjectType : String
)

data class RoomResponse(
    @SerializedName("RoomList") val RoomList: ArrayList<Room>
)