package com.sqsong.valuecomponent.dialog

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sqsong.valuecomponent.R
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog : DialogFragment() {

    private var mAnimator: ValueAnimator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        val text = getString(R.string.text_loading)
        loadingText.setText(SpannableString(text), TextView.BufferType.SPANNABLE)
        val colorSpan = ForegroundColorSpan(Color.TRANSPARENT)
        val spannable = loadingText.text as Spannable
        mAnimator = ValueAnimator.ofInt(0, 4).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 1000
            addUpdateListener {
                val dotsCount = it.animatedValue as Int
                if (dotsCount < 4) {
                    spannable.setSpan(
                        colorSpan,
                        text.length - 3 + dotsCount,
                        loadingText.text.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    loadingText.invalidate()
                }
            }
        }
        mAnimator?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAnimator?.cancel()
        mAnimator = null
    }

}
