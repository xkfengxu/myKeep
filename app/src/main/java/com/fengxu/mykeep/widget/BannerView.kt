package com.fengxu.mykeep.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.fengxu.mykeep.R
import com.fengxu.mykeep.widget.banner.BaseIndicator
import com.fengxu.mykeep.widget.banner.CircleIndicator
import com.fengxu.mykeep.widget.banner.ScalePageTransformer
import java.util.*

/**
 * @author Created by YANG on 2018/8/31.
 * @description  原：com.example.zjy.zjywidget
 */
class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    /**
     * 轮播图ViewPager
     */
    private var mBannerViewPager: ViewPager? = null
    /**
     * 轮播指示器
     */
    private var mIndicator: BaseIndicator? = null
    /**
     * 是否自动滑动
     */
    private var mIsAutoScroll = false
    /**
     * 默认页面之间自动切换的时间间隔
     */
    private var mDelayTime: Long = 0
    /**
     * 轮播图地址集合
     */
    private var mBannerUrlList: MutableList<String>? = null
    /**
     * 轮播内部Adapter,实现无限轮播
     */
    private var mAdapter: InnerPagerAdapter? = null
    /**
     * 当前真正的下标，初始值为MAX的一半，以解决初始无法左滑的问题
     */
    private var mCurrentIndex = Int.MAX_VALUE / 2
    /**
     * 图片之间的边距
     */
    private var mPageMargin = 0
    /**
     * 是否启用边距模式（同时显示部分左右Item）
     */
    private var mIsMargin = false
    /**
     * Banner圆角
     */
    private var mBannerRadius = 0f
    //播放标志
    private var isPlay = false

    //触发轮播的消息标志位
    companion object {
        const val PLAY = 0x123
    }

    //标志是否是手势操作造成的滑动
    private var mGestureScroll = false
    /**
     * 轮播计时器
     */
    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == PLAY && mIsAutoScroll) {
                mBannerViewPager?.currentItem = mCurrentIndex
                if (isPlay) {
                    playView()
                }
            }
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (mIsMargin) {
            clipChildren = false
        }
        initBannerViewPager(context, attrs)
        initIndicatorView(context)
    }

    private fun handleStyleable(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) {
        val ta =
            context.theme.obtainStyledAttributes(attrs, R.styleable.BannerView, defStyle, 0)
        try {
            mIsAutoScroll = ta.getBoolean(R.styleable.BannerView_banner_auto_scroll, true)
            mPageMargin = ta.getDimensionPixelSize(R.styleable.BannerView_banner_page_margin, 0)
            mDelayTime = ta.getInteger(R.styleable.BannerView_banner_toggle_duration, 2000).toLong()
            mBannerRadius =
                ta.getDimensionPixelSize(R.styleable.BannerView_banner_radius, 0).toFloat()
            if (mPageMargin > 0) {
                mIsMargin = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ta.recycle()
        }
    }

    /**
     * 初始化ViewPager
     * @param context
     * @param attrs
     */
    private fun initBannerViewPager(
        context: Context,
        attrs: AttributeSet?
    ) {
        mBannerViewPager = ViewPager(context, attrs)

        val bannerParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (mIsMargin) {
            bannerParams.setMargins(mPageMargin, dp2px(16f), mPageMargin, dp2px(16f))
        }
        addView(mBannerViewPager, bannerParams)
        mBannerViewPager?.addOnPageChangeListener(mPageListener)
        mBannerUrlList = ArrayList()
        mAdapter = InnerPagerAdapter()
        mBannerViewPager?.adapter = mAdapter
        mBannerViewPager?.currentItem = Int.MAX_VALUE / 2
        mBannerViewPager?.setPageTransformer(true, ScalePageTransformer())
        if (mIsMargin) {
            mBannerViewPager?.offscreenPageLimit = 5
            mBannerViewPager?.pageMargin = mPageMargin / 2
            mBannerViewPager?.clipChildren = false
        }
    }

    /**
     * 初始化指示器
     * @param context
     */
    private fun initIndicatorView(context: Context) {
        mIndicator = CircleIndicator(context)
        val indicatorParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(60f))
        indicatorParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        addView(mIndicator, indicatorParams)
    }

    fun setIndicator(indicator: BaseIndicator?) {
        removeView(mIndicator)
        mIndicator = indicator
        val indicatorParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(60f))
        indicatorParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        addView(mIndicator, indicatorParams)
        invalidate()
    }

    /**
     * ViewPager滑动监听
     */
    private var mPageListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) { //此处返回的position是大数
                if (position == 0 || mBannerUrlList!!.isEmpty()) {
                    return
                }
                setScrollPosition(position)
                val smallPos = position % mBannerUrlList!!.size
                mIndicator?.setCurrentPosition(smallPos)
                if (mScrollPageListener != null) {
                    mScrollPageListener!!.onPageSelected(smallPos)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                onPageScrollStateChange(state)
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = if (hMode == MeasureSpec.UNSPECIFIED || hMode == MeasureSpec.AT_MOST) {
            dp2px(200f)
        } else {
            hSize
        }
        val heightMeasure = MeasureSpec.makeMeasureSpec(
            height,
            MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    /**
     * 设置Banner图片地址数据
     * @param bannerData
     */
    fun setBannerData(bannerData: MutableList<String>) {
        mBannerUrlList!!.clear()
        mBannerUrlList!!.addAll(bannerData)
        mAdapter?.notifyDataSetChanged()
        startPlay(mDelayTime)
        mIndicator?.setCellCount(bannerData.size)
    }

    /**
     * 开始播放
     *
     * @param delayMillis
     */
    private fun startPlay(delayMillis: Long) {
        isPlay = true
        mDelayTime = delayMillis
        playView()
    }

    private fun playView() {
        mCurrentIndex++
        mHandler.sendEmptyMessageDelayed(PLAY, mDelayTime)
    }

    /**
     * 手指滑动时暂停自动轮播，手指松开时重新启动自动轮播
     *
     * @param state
     */
    private fun onPageScrollStateChange(state: Int) {
        if (!mIsAutoScroll) {
            return
        }
        when (state) {
            ViewPager.SCROLL_STATE_IDLE -> {
                if (!mGestureScroll) {
                    return
                }
                mGestureScroll = false
                mHandler.removeMessages(PLAY)
                mHandler.sendEmptyMessageDelayed(PLAY, 100)
            }
            ViewPager.SCROLL_STATE_DRAGGING -> {
                // 手指滑动时，清除播放下一张，防止滑动过程中自动播放下一张
                mGestureScroll = true
                mHandler.removeMessages(PLAY)
            }
            else -> {
            }
        }
    }

    /**
     * 滑动的时候更新下标
     *
     * @param position
     */
    fun setScrollPosition(position: Int) {
        mCurrentIndex = position
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(PLAY)
    }

    /**
     * 内部Adapter，包装setAdapter传进来的adapter，设置getCount返回Integer.MAX_VALUE
     */
    private inner class InnerPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return Int.MAX_VALUE
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        }

        override fun instantiateItem(container: ViewGroup, p: Int): Any {
            var position = p
            val cardView = CardView(context)
            cardView.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            cardView.cardElevation = 5F
            cardView.radius = mBannerRadius
            val bannerIv = ImageView(context)
            bannerIv.scaleType = ImageView.ScaleType.CENTER_CROP
            bannerIv.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val size = mBannerUrlList!!.size
            if (size == 0) {
                return bannerIv
            }
            position %= size
            if (position < 0) {
                position += size
            }
            Glide.with(context).load(mBannerUrlList!![position]).into(bannerIv)
            cardView.addView(bannerIv)
            container.addView(cardView)
            //bannerIv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return cardView
        }

        override fun isViewFromObject(view: View, any: Any): Boolean {
            return view === any
        }
    }

    /**
     * dp转px
     * @param dpVal   dp value
     * @return px value
     */
    private fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal,
            context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 滚动监听回调接口
     */
    var mScrollPageListener: ScrollPageListener? = null

    interface ScrollPageListener {
        fun onPageSelected(position: Int)
    }

    init {
        handleStyleable(context, attrs, defStyleAttr)
        init(context, attrs)
    }
}