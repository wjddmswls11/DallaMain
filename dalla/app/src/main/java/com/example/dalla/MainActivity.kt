package com.example.dalla

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.dalla.databinding.ActivityMainBinding
import com.example.dalla.adapter.BannerAdapter
import com.example.dalla.adapter.NowLiveAdapter
import com.example.dalla.adapter.CircleImgAdapter
import com.example.dalla.adapter.NowTopAdapter
import com.example.dalla.adapter.ProfilePictureAdapter
import com.example.dalla.databinding.ItemProfilepictureBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var shouldChangeIcons = false

    private var eventBannerPosition = 0

    private val handler = Handler(Looper.getMainLooper()) {
        true
    }
    private lateinit var mainViewModel: DallaViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val nowTopAdapter = NowTopAdapter()
    private val nowLiveAdapter = NowLiveAdapter()
    private val circleImgAdapter = CircleImgAdapter()
    private val bannerAdapter = BannerAdapter()
    private val profilePictureAdapter = ProfilePictureAdapter()

    private val scope = CoroutineScope(Dispatchers.Main)

    private val scrollJobs = HashMap<ViewPager2, Job>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //탭레이아웃추가
        binding.tablayoutFirst.apply {
            addTab(newTab().setText("전체"))
            addTab(newTab().setText("VIDEO"))
            addTab(newTab().setText("RADIO"))
            addTab(newTab().setText("신입BJ"))
        }


        //스크롤뷰 내에서 탭레이아웃과 툴바가 올바르게 동작
        binding.stickScrollView.apply {
            header = binding.tablayoutFirst
            externalToolbar = binding.toolBar
        }

        //DallaViewModel의 인스턴스를 생성하는 코드
        mainViewModel = ViewModelProvider(this)[DallaViewModel::class.java]

        //바텀네비게이션 보라색으로 나오던 것
        binding.mainBtn.itemIconTintList = null

        //액션바를 숨기고 툴바를 액션바로 설정
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        //레이아웃의 화면을 전체화면으로 만들고 기본 네비게이션 바의 높이만큼 올려준다
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            binding.root.setOnApplyWindowInsetsListener { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
                view.updatePadding(bottom = insets.bottom)
                WindowInsets.CONSUMED
            }
        }

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                updateLayoutView()
                swipeRefreshLayout.isRefreshing = false
            }
        }


        setOnScrollChangeListener()
        setObservable()
        setObservable()
        setAdapter()
    }




    //액티비티에 메뉴를 추가하려면  구현해야 하는 함수
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.live_menu, menu)
        return true
    }

    // 기존의 onPrepareOptionsMenu 메소드
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        updateToolbarIcon(menu, shouldChangeIcons)
        return super.onPrepareOptionsMenu(menu)
    }


    // 툴바 아이콘을 변경하는 메소드
    private fun updateToolbarIcon(menu: Menu, isLight: Boolean) {
        val storeIcon = if (isLight) R.drawable.btn_store_b else R.drawable.btn_store_w
        val rankingIcon = if (isLight) R.drawable.btn_ranking_b else R.drawable.btn_ranking_w
        val messageIcon = if (isLight) R.drawable.btn_message_b else R.drawable.btn_message_w
        val alarmIcon = if (isLight) R.drawable.btn_alarm_b else R.drawable.btn_alarm_w

        menu.findItem(R.id.btn_store)?.setIcon(storeIcon)
        menu.findItem(R.id.btn_ranking)?.setIcon(rankingIcon)
        menu.findItem(R.id.btn_message)?.setIcon(messageIcon)
        menu.findItem(R.id.btn_alarm)?.setIcon(alarmIcon)
    }


    //일정 시간 간격으로 한 페이지씩 이동
    private fun startScroll(viewPager: ViewPager2, delay: Long) {
        stopScroll(viewPager)

        scrollJobs[viewPager] = scope.launch {
            while (isActive) {
                delay(delay)
                val nextItem = (viewPager.currentItem + 1) % (bannerAdapter.itemCount)
                viewPager.setCurrentWithDuration(nextItem, SCROLL_DURATION)
            }
        }
    }

    private fun stopScroll(viewPager: ViewPager2) {
        scrollJobs[viewPager]?.cancel()
        scrollJobs.remove(viewPager)
    }

    //자동스크롤의 애니메이션을 조절
    private fun ViewPager2.setCurrentWithDuration(item: Int, duration: Long) {
        val pxTopDrag = width * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxTopDrag)

        animator.addUpdateListener(object : AnimatorUpdateListener {
            var previousValue = 0
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val currentValue = valueAnimator.animatedValue as Int
                val currentPxToDrag = (currentValue - previousValue).toFloat()
                fakeDragBy(-currentPxToDrag)
                previousValue = currentValue
            }
        })

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                beginFakeDrag()
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                endFakeDrag()
            }
        })

        animator.duration = duration
        animator.start()
    }


    //Activity가 전면에 나타나면 자동 스크롤이 시작
    override fun onResume() {
        super.onResume()
        startScroll(binding.profilePictureView, PROFILE_PICTURE_SCROLL_DELAY)
        startScroll(binding.BannerViewPager, BANNER_SCROLL_DELAY)
    }

    //액티비티가 화면에서 사라질 때 자동 스크롤이 계속 진행되지 않도록 방지
    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateLayoutView() {
        mainViewModel.refreshData()
    }


    private fun setOnScrollChangeListener() {
        //뷰페이저높이의 1/3부터 시작해서 뷰페이저까지 점점 알파값이 커지며 하얗게 되는 메소드
        with(binding) {
            stickScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val viewPagerHeight = profilePictureView.height
                val viewPagerY = profilePictureView.y + viewPagerHeight - (viewPagerHeight * 1 / 3)
                val maxScrollY = profilePictureView.y + viewPagerHeight

                val alpha: Int = if (scrollY >= maxScrollY) {
                    255
                } else if (scrollY >= viewPagerY) {
                    val progress = (scrollY - viewPagerY) / (maxScrollY - viewPagerY)
                    Log.d("scrollY - viewPagerY", "onCreate: ${scrollY - viewPagerY}")
                    Log.d("maxScrollY - viewPagerY", "onCreate: ${maxScrollY - viewPagerY}")
                    (progress * 255).toInt()
                } else {
                    0
                }
                toolBar.setBackgroundColor(Color.argb(alpha, 255, 255, 255))
                imgToolbar.visibility = if (alpha > 20) View.VISIBLE else View.INVISIBLE
                shouldChangeIcons = alpha > 0
                invalidateOptionsMenu()
            }
        }
    }

    companion object {
        const val PROFILE_PICTURE_SCROLL_DELAY = 6000L
        const val BANNER_SCROLL_DELAY = 4000L
        const val SCROLL_DURATION = 500L
    }

    private fun setObservable() {
        collectProfileSharedFlow()
        collectMyStarSharedFlow()
        collectEventSharedFlow()
        collectRoomSharedFlow()
        collectNowTopTenSharedFlow()
    }

    private fun setAdapter() {

        val itemDecoration = CustomItemDecoration(dpToPx(6))


        binding.profilePictureView.apply {
            adapter = profilePictureAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    //현재 페이지가 마지막 페이지이면 처음으로 돌아가기 위해 아래 코드를 실행합니다.
                    if (position == Int.MAX_VALUE - 1) {
                        setCurrentItem(0, false)
                    }
                }
            })
            //사진과 글자가 따로 갈 수 있도록
            setPageTransformer { page, position ->
                val binding = ItemProfilepictureBinding.bind(page)
                binding.clsItemprofile.translationX = position * page.width / 2
            }
        }

        binding.rclCircle.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = circleImgAdapter
            addItemDecoration(itemDecoration)
        }

        binding.BannerViewPager.apply {
            adapter = bannerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    eventBannerPosition = position
                    binding.txtCurrentBanner.text = getString(
                        R.string.viewpager2_banner_ko,
                        (eventBannerPosition % bannerAdapter.dataItemCount) + 1,
                        bannerAdapter.dataItemCount
                    )
                    if (position == Int.MAX_VALUE - 1) {
                        setCurrentItem(0, false)
                    }
                }
            })
        }


        binding.rcvNowLive.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = nowLiveAdapter
        }


        binding.rclNowTop.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = nowTopAdapter
        }

    }






    private fun collectProfileSharedFlow() {
        mainViewModel.viewModelScope.launch {
            mainViewModel.profileSharedFlow.collectLatest { profileSharedFlow ->
                with(binding.profilePictureView) {
                    Log.d("MainActivity", "Banner List: $profileSharedFlow")
                    profilePictureAdapter.setData(profileSharedFlow)

                    //Int.MAX_VALUE의 가운데에서 시작하도록 설정합니다.
                    val start =
                        if (profilePictureAdapter.dataItemCount > 0) Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % profilePictureAdapter.dataItemCount) else 0
                    if (!binding.profilePictureView.isFakeDragging) {
                        setCurrentItem(start, false)
                    }
                }
            }
        }
    }


    private fun collectMyStarSharedFlow() {
        mainViewModel.viewModelScope.launch {
            mainViewModel.myStarSharedFlow.collectLatest { myStarSharedFlow ->
                Log.d("MainActivity", "MyStar List: $myStarSharedFlow")
                circleImgAdapter.setData(myStarSharedFlow)
            }
        }
    }

    private fun collectEventSharedFlow() {
        mainViewModel.viewModelScope.launch {
            mainViewModel.eventSharedFlow.collectLatest { collectEventSharedFlow ->
                //가져온 Event데이터를 처리하고 화면에 표시하는 코드를 담고 있습니다.
                Log.d("MainActivity", "eventBanner List: $collectEventSharedFlow")
                bannerAdapter.setData(collectEventSharedFlow)

                // 데이터를 설정한 후에 가운데에서 시작하는 위치를 계산하고 설정합니다.
                val start =
                    if (bannerAdapter.dataItemCount > 0) Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerAdapter.dataItemCount) else 0

                with(binding.BannerViewPager) {
                    if (!binding.BannerViewPager.isFakeDragging) {
                        setCurrentItem(start, false)
                    }
                }

                // with(binding) 블록 외부에서 txtCurrentBanner에 접근하여 초기 텍스트 설정
                binding.txtCurrentBanner.text =
                    getString(R.string.viewpager2_banner_ko, 1, collectEventSharedFlow.size)
            }
        }
    }


    private fun collectRoomSharedFlow() {
        //rcvNowLive의 adapter를 설정
        mainViewModel.viewModelScope.launch {
            mainViewModel.roomSharedFlow.collectLatest { roomSharedFlow ->
                // rcvNowLive의 adapter를 설정
                Log.d("MainActivity roomLiveData", "room List: $roomSharedFlow")
                nowLiveAdapter.setData(roomSharedFlow)
            }
        }
    }


    private fun collectNowTopTenSharedFlow() {
        mainViewModel.nowTopTenLiveData.observe(this) { nowTopTenLiveData ->
            Log.d("TAG nowTopTenLiveData", "setObservable: $nowTopTenLiveData")
            nowTopAdapter.setData(nowTopTenLiveData)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }


}