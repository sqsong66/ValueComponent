package com.sqsong.valuecomponent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.common.GlideApp
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_viewpager_transformer.*

class ViewPagerTransformerFragment : Fragment() {

    companion object {
        const val IMAGE_URL = "image_url"

        fun newInstance(imageUrl: String): ViewPagerTransformerFragment {
            return ViewPagerTransformerFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL, imageUrl)
                }
            }
        }
    }

    private var mImageUrl: String? = null

    /*private fun hideSystemUI() {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageUrl = arguments?.getString(IMAGE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewpager_transformer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        imageTouch.displayType = ImageViewTouchBase.DisplayType.FIT_WIDTH
        GlideApp.with(this).load(mImageUrl).into(imageTouch)

        imageTouch.setSingleTapListener {
            if (activity is ViewPagerTransformerActivity) {
                (activity as ViewPagerTransformerActivity).performSystemUI()
            }
        }
    }

}