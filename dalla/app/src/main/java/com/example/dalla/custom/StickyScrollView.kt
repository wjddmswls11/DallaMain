package com.example.dalla.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalla.R

class StickyScrollView : NestedScrollView, ViewTreeObserver.OnGlobalLayoutListener {
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var header: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
            }
        }

    private var mHeaderInitPosition = 0f
    private var mRecyclerviewNowTop10Height = 0
    private var mRecyclerviewMyStarHeight = 0
    private var rclNowTop: RecyclerView? = null
    private var rclCircle: RecyclerView? = null
    private var rclNowTopHeight = 0
    private var rclCircleHeight = 0
    var externalToolbar: androidx.appcompat.widget.Toolbar? = null

    //헤더의 초기 위치 설정 및 이벤트 제거
    override fun onGlobalLayout() {
        // mHeaderInitPosition을 headerY와 headerHeight 합으로 설정합니다.
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
        rclNowTopHeight =  rclNowTop?.height ?: 0
        Log.d("TAG height", "onGlobalLayout: $rclNowTopHeight")
        rclCircleHeight = rclCircle?.height ?: 0
        Log.d("TAG height", "onGlobalLayout: $rclCircleHeight")

        // ViewTreeObserver에서 이벤트를 제거합니다.
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }


    //뷰가 창에 연결될 때 호출되는 메서드
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rclNowTop = findViewById(R.id.rcl_NowTop)
        rclCircle = findViewById(R.id.rcl_Circle)
        rclNowTop?.viewTreeObserver?.addOnGlobalLayoutListener {
            rclNowTop?.let {
                mRecyclerviewNowTop10Height = it.height
                Log.d("mRecyclerviewHeight", "onAttachedToWindow: $mRecyclerviewNowTop10Height")
            }
        }



        rclCircle?.viewTreeObserver?.addOnGlobalLayoutListener {
            rclCircle?.let {
                mRecyclerviewMyStarHeight = it.height
                Log.d("mrecviewStarbjHeight", "onAttachedToWindow: $mRecyclerviewMyStarHeight")
            }
        }
    }

    //스크롤이 변경될 때 호출되는 메서드
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val toolbarHeightPx = externalToolbar?.height?.toFloat() ?: 0f
        Log.d("toolbar", "onScrollChanged: $toolbarHeightPx")
        Log.d("all","$mHeaderInitPosition, $mRecyclerviewNowTop10Height, $mRecyclerviewMyStarHeight")

        if (rclNowTopHeight == 0){
            rclNowTopHeight = mRecyclerviewNowTop10Height
            mHeaderInitPosition += rclNowTopHeight
            Log.d("ddd a", "onScrollChanged: $mRecyclerviewNowTop10Height")

        } else if(rclCircleHeight == 0){
            rclCircleHeight = mRecyclerviewMyStarHeight
            mHeaderInitPosition += rclCircleHeight
            Log.d("ddd b", "onScrollChanged: $mRecyclerviewMyStarHeight")
        }

        if (t > mHeaderInitPosition - toolbarHeightPx) {
            stickHeader(t - mHeaderInitPosition + toolbarHeightPx)
        } else {
            freeHeader()
        }
    }
    //헤더의 위치를 조정하는데 사용되는 보조 메서드
    private fun stickHeader(position: Float) {
        header?.translationY = position
    }

    //헤더를 원래 위치로 되돌림
    private fun freeHeader() {
        header?.translationY = 0f
    }
}