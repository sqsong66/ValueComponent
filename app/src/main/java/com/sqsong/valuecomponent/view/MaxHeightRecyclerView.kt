package com.sqsong.valuecomponent.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.valuecomponent.R

class MaxHeightRecyclerView : RecyclerView {

    private var mMaxHeight = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        var ta: TypedArray? = null
        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
            mMaxHeight = ta.getDimension(R.styleable.MaxHeightRecyclerView_mhr_maxHeight, .0f).toInt()
        } finally {
            ta?.recycle()
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var spec = heightSpec
        if (mMaxHeight > 0) {
            spec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthSpec, spec)
    }

}
