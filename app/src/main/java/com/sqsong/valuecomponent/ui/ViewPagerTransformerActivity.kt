package com.sqsong.valuecomponent.ui

import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.common.DepthPageTransformer
import com.sqsong.valuecomponent.common.SimpleFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_viewpager_transformer.*
import java.io.File

class ViewPagerTransformerActivity : AppCompatActivity() {

    private var isSystemUIShow = true

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager_transformer)

        initEvent()
    }

    private fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val path = Environment.getExternalStorageDirectory().absolutePath + "/image"
        val imageFile = File(path)
        if (!imageFile.exists()) {
            finish()
            return
        }

        val imageList = imageFile.listFiles { file ->
            return@listFiles (file?.absolutePath?.endsWith(".jpg") == true || file?.absolutePath?.endsWith(
                ".png"
            ) == true
                    || file?.absolutePath?.endsWith(".jpeg") == true)
        }
        if (imageList.isEmpty()) {
            finish()
            return
        }

        val fragmentList = mutableListOf<Fragment>()
        imageList.forEach {
            fragmentList.add(ViewPagerTransformerFragment.newInstance(it.absolutePath))
        }
        val pagerAdapter =
            SimpleFragmentPagerAdapter<Fragment>(supportFragmentManager, fragmentList)
        viewPager.setPageTransformer(true, DepthPageTransformer())
        viewPager.adapter = pagerAdapter

        toolbar.post { toolbar.title = "1/${fragmentList.size}" }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                toolbar.title = "${position + 1}/${fragmentList.size}"
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun performSystemUI() {
//        if (isSystemUIShow) {
//            hideSystemUI()
//            toolbar.visibility = View.INVISIBLE
//        } else {
//            showSystemUI()
//            toolbar.visibility = View.VISIBLE
//        }
//        isSystemUIShow = !isSystemUIShow
    }

}