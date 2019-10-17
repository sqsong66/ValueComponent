package com.sqsong.valuecomponent.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import com.sqsong.datepicker.utils.DensityUtils
import com.sqsong.valuecomponent.R

data class HistogramData(
    val name: String,
    val percent: Float
)

class FoodSafetyHistogram : View {

    private var sideGap = .0f
    private var nameColor = 0
    private var nameSize = .0f
    private var percentColor = 0
    private var verticalGap = .0f
    private var percentSize = .0f
    private var histogramColor = 0
    private var horizontalGap = .0f
    private var bottomLineColor = 0
    private var histogramWidth = .0f
    private var histogramRadius = .0f
    private var animateDuration = 500L
    private var bottomLineHeight = .0f

    private val mTempRect = Rect()
    private val mTempPath = Path()
    private var isAnimating = false
    private lateinit var mPaint: Paint
    private var mCurrentPercent = 1.0f
    private var mValueAnimator: ValueAnimator? = null
    private val mDataList = mutableListOf<HistogramData>().apply {
        add(HistogramData("水果", 0.5f))
        add(HistogramData("蔬菜", 0.8f))
        add(HistogramData("水产品", 0.65f))
        add(HistogramData("畜禽肉类", 0.35f))
        add(HistogramData("副食干货", 0.25f))
        add(HistogramData("腌渍品", 0.58f))
        add(HistogramData("其他食品", 1.0f))
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttr(context, attrs)
        initParams()
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.FoodSafetyHistogram)
            histogramWidth = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_width,
                DensityUtils.dip2px(20).toFloat()
            )
            verticalGap = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_vertical_gap,
                DensityUtils.dip2px(16).toFloat()
            )
            horizontalGap = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_horizontal_gap,
                DensityUtils.dip2px(20).toFloat()
            )
            sideGap = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_side_gap,
                DensityUtils.dip2px(10).toFloat()
            )
            percentColor = typedArray.getColor(
                R.styleable.FoodSafetyHistogram_fsh_percent_color,
                ContextCompat.getColor(context, R.color.color333333)
            )
            percentSize = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_percent_size,
                DensityUtils.dip2px(12).toFloat()

            )
            nameColor = typedArray.getColor(
                R.styleable.FoodSafetyHistogram_fsh_name_color,
                ContextCompat.getColor(context, R.color.color999999)
            )
            nameSize = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_name_size,
                DensityUtils.dip2px(12).toFloat()

            )
            histogramColor = typedArray.getColor(
                R.styleable.FoodSafetyHistogram_fsh_histogram_color,
                ContextCompat.getColor(context, R.color.colorF08300)
            )
            histogramRadius = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_histogram_radius,
                DensityUtils.dip2px(5).toFloat()
            )
            bottomLineColor = typedArray.getColor(
                R.styleable.FoodSafetyHistogram_fsh_bottom_line_color,
                ContextCompat.getColor(context, R.color.colorF1F1F1)
            )
            bottomLineHeight = typedArray.getDimension(
                R.styleable.FoodSafetyHistogram_fsh_bottom_line_height,
                DensityUtils.dip2px(1).toFloat()
            )
            animateDuration =
                typedArray.getInt(R.styleable.FoodSafetyHistogram_fsh_animate_duration, 500)
                    .toLong()
        } finally {
            typedArray?.recycle()
        }
    }

    private fun initParams() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        mPaint.isDither = true
    }

    private fun initValueAnimator() {
        mValueAnimator = ValueAnimator.ofFloat(.0f, 1.0f).setDuration(animateDuration)
        mValueAnimator?.interpolator = AccelerateDecelerateInterpolator()
        mValueAnimator?.addUpdateListener {
            mCurrentPercent = it.animatedValue as Float
            postInvalidate()
        }
        mValueAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                isAnimating = true
                mCurrentPercent = .0f
            }

            override fun onAnimationEnd(animation: Animator?) {
                isAnimating = false
                mCurrentPercent = 1.0f
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.textSize = nameSize
        val text = "测试"
        mPaint.getTextBounds(text, 0, text.length, mTempRect)
        val bottomHeight = mTempRect.height() + 2 * verticalGap
        canvas?.translate(.0f, height - bottomHeight)
        drawLine(canvas)
        drawText(canvas, bottomHeight)
    }

    private fun drawLine(canvas: Canvas?) {
        mPaint.color = bottomLineColor
        mPaint.strokeWidth = bottomLineHeight
        canvas?.drawLine(.0f, .0f, width.toFloat(), 0.0f, mPaint)
    }

    private fun drawText(canvas: Canvas?, topHeight: Float) {
        if (mDataList.isEmpty()) return
        val gap = (width - sideGap * 2 - histogramWidth * mDataList.size) / (mDataList.size - 1)
        val totalHeight = -(height - topHeight * 2)
        for (index in 0 until mDataList.size) {
            val startX = sideGap + (histogramWidth + gap) * index
            val histogramHeight = totalHeight * mDataList[index].percent * mCurrentPercent
            mTempPath.reset()
            mTempPath.moveTo(startX, .0f)
            mTempPath.lineTo(startX, histogramHeight + histogramRadius)
            mTempPath.quadTo(startX, histogramHeight, startX + histogramRadius, histogramHeight)
            mTempPath.lineTo(startX + histogramWidth - histogramRadius, histogramHeight)
            mTempPath.quadTo(
                startX + histogramWidth,
                histogramHeight,
                startX + histogramWidth,
                histogramHeight + histogramRadius
            )
            mTempPath.lineTo(startX + histogramWidth, .0f)
            mTempPath.close()
            mPaint.color = histogramColor
            canvas?.drawPath(mTempPath, mPaint) // 绘制柱状图

            val centerX = startX + histogramWidth / 2
            mPaint.color = nameColor
            mPaint.textSize = nameSize
            val text = mDataList[index].name
            val x = centerX - mPaint.measureText(text) / 2
            val baseLine = topHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2
            canvas?.drawText(text, x, baseLine, mPaint) // 绘制底部名称

            val percentText = "${mDataList[index].percent * 100}%"
            mPaint.color = percentColor
            mPaint.textSize = percentSize
            val px = centerX - mPaint.measureText(percentText) / 2
            canvas?.drawText(percentText, px, histogramHeight - verticalGap, mPaint) // 绘制百分比
        }
    }

    fun setHistogramData(dataList: List<HistogramData>, animate: Boolean) {
        if (dataList.isEmpty()) return
        mDataList.clear()
        mDataList.addAll(dataList)
        if (animate) {
            if (isAnimating) return
            if (mValueAnimator == null) initValueAnimator()
            mValueAnimator?.start()
        } else {
            mCurrentPercent = 1.0f
            postInvalidate()
        }
    }
}
