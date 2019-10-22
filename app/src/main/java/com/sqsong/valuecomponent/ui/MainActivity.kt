package com.sqsong.valuecomponent.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.sqsong.datepicker.DatePickerDialog
import com.sqsong.datepicker.utils.WheelDateUtils
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.dialog.LoadingDialog
import com.sqsong.valuecomponent.view.HistogramData
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateActionListener {

    private var mUnbinder: Unbinder? = null
    private var mRxPermissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUnbinder = ButterKnife.bind(this)

        mRxPermissions = RxPermissions(this)
    }

    @OnClick(
        R.id.date_picker_btn,
        R.id.loading_btn,
        R.id.histogram_btn,
        R.id.address_btn,
        R.id.viewPagerTransformerBtn
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.date_picker_btn -> showDatePicker()
            R.id.loading_btn -> showLoading()
            R.id.histogram_btn -> setHistogramData()
            R.id.address_btn -> startActivity(Intent(this, AddressActivity::class.java))
            R.id.viewPagerTransformerBtn -> {
                mRxPermissions?.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.subscribe {
                        if (it) {
                            startActivity(Intent(this, ViewPagerTransformerActivity::class.java))
                        } else {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun setHistogramData() {
        val dataList = mutableListOf<HistogramData>().apply {
            add(HistogramData("水果", 0.5f))
            add(HistogramData("蔬菜", 0.8f))
            add(HistogramData("水产品", 0.65f))
            add(HistogramData("畜禽肉类", 0.35f))
            add(HistogramData("副食干货", 0.25f))
            add(HistogramData("腌渍品", 0.58f))
            add(HistogramData("其他食品", 1.0f))
        }
        histogramView.setHistogramData(dataList, true)
    }

    private fun showLoading() {
        LoadingDialog().show(supportFragmentManager, "")
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val endMillis = calendar.timeInMillis
        val year = calendar.get(Calendar.YEAR)
        calendar.set(Calendar.YEAR, year - 20)
        val startMillis = calendar.timeInMillis
        DatePickerDialog.newInstance(
            startMillis,
            endMillis,
            endMillis,
            WheelDateUtils.MODE_YMDHM,
            0
        ).show(supportFragmentManager, "")
    }

    override fun onDateArrived(
        year: String?,
        month: String?,
        day: String?,
        hour: String?,
        minute: String?,
        second: String?,
        extraType: Int
    ) {
        val time = "$year-$month-$day $hour:$minute:$second"
        Toast.makeText(this, "Time: $time", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogDismiss(extraType: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder?.unbind()
    }

}
