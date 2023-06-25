package com.example.dalla

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalla.data.Banner
import com.example.dalla.data.Event
import com.example.dalla.data.MyStar
import com.example.dalla.data.NowTop10
import com.example.dalla.data.PageNoData
import com.example.dalla.data.Room
import com.example.dalla.retrofit.NetworkManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class DallaViewModel : ViewModel() {

    private val service = NetworkManager.retrofit()

    private val _profileSharedFlow = MutableSharedFlow<ArrayList<Banner>>()
    val profileSharedFlow: SharedFlow<ArrayList<Banner>>
        get() = _profileSharedFlow

    private val _myStarSharedFlow = MutableSharedFlow<ArrayList<MyStar>>()
    val myStarSharedFlow: SharedFlow<ArrayList<MyStar>>
        get() = _myStarSharedFlow

    private val _eventSharedFlow = MutableSharedFlow<ArrayList<Event>>()
    val eventSharedFlow: SharedFlow<ArrayList<Event>>
        get() = _eventSharedFlow

    private val _roomSharedFlow = MutableSharedFlow<ArrayList<Room>>()
    val roomSharedFlow: SharedFlow<ArrayList<Room>>
        get() = _roomSharedFlow

    private val _nowTopTenLiveData = MutableLiveData<ArrayList<NowTop10>>()
    val nowTopTenLiveData: LiveData<ArrayList<NowTop10>>
        get() = _nowTopTenLiveData


    init {
        fetchBannerData()
        fetchMyStarData()
        fetchEventData()
        postRoomList()
        nowTopTenList()
    }

    fun refreshData(){
        viewModelScope.launch {
            fetchBannerData()
            fetchMyStarData()
            fetchEventData()
            postRoomList()
            nowTopTenList()
        }
    }



    //Retrofit을 사용하여 서버에서 Banner데이터를 가져오는 함수
    private fun fetchBannerData() {
        viewModelScope.launch {
            try {
                val response = service.getRetrofitData()
                response.bannerList.let {
                    Log.d("DallaViewModel", "Response: ${response.bannerList}")
                    _profileSharedFlow.emit(response.bannerList)
                }
            } catch (e: Exception) {
                Log.e("DallaViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }



    //Retrofit을 사용하여 서버에서 Mystar데이터를 가져오는 함수
    private fun fetchMyStarData() {
        viewModelScope.launch {
            try {
                val response = service.getMyStarData()
                response.starList.let {
                    _myStarSharedFlow.emit(it)
                }
            } catch (e: Exception) {
                Log.e("DallaViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }


    //Retrofit을 사용하여 서버에서 Event데이터를 가져오는 함수
    private fun fetchEventData() {
        viewModelScope.launch {
            try {
                val response = service.getEventData()
                response.eventBannerList.let {
                    _eventSharedFlow.emit(it)
                }
            } catch (e: Exception) {
                Log.e("DallaViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }

    //Post방식으로 서버에 요청을 보내는 함수
    private fun postRoomList() {
        viewModelScope.launch {
            try {
                val response = service.postRoomList(PageNoData(PageNo = 1))
                response.RoomList.let {
                    _roomSharedFlow.emit(it)
                }
            } catch (e: Exception) {
                Log.e("DallaViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }


    private fun nowTopTenList() {
        val nowTopTenList = arrayListOf(
            NowTop10(
                R.drawable.number_w_1,
                "달대리찡",
                "https://cdn.pixabay.com/photo/2018/03/06/22/57/portrait-3204843_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_2,
                "이솔",
                "https://cdn.pixabay.com/photo/2015/03/03/20/42/man-657869_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_3,
                "사탕",
                "https://cdn.pixabay.com/photo/2016/06/06/17/05/woman-1439909_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_4,
                "Giyoo",
                "https://cdn.pixabay.com/photo/2015/01/12/10/44/woman-597173_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_5,
                "츄즈",
                "https://cdn.pixabay.com/photo/2018/04/27/03/50/portrait-3353699_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_6,
                "말쑤",
                "https://cdn.pixabay.com/photo/2016/11/21/12/42/beard-1845166_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_7,
                "웈언니",
                "https://cdn.pixabay.com/photo/2017/06/26/02/47/man-2442565_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_8,
                "해나잉뎅",
                "https://cdn.pixabay.com/photo/2019/11/03/05/36/portrait-4597853_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_9,
                "DIOR",
                "https://cdn.pixabay.com/photo/2015/05/20/12/45/girl-775349_640.jpg"
            ),
            NowTop10(
                R.drawable.number_w_10,
                "이호익",
                "https://cdn.pixabay.com/photo/2019/02/11/20/20/woman-3990680_640.jpg"
            )
        )
        _nowTopTenLiveData.value = nowTopTenList
    }
}