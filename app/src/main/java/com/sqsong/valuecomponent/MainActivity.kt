package com.sqsong.valuecomponent

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.sqsong.datepicker.DatePickerDialog
import com.sqsong.datepicker.utils.WheelDateUtils
import com.sqsong.valuecomponent.dialog.LoadingDialog
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateActionListener {

    private var mUnbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUnbinder = ButterKnife.bind(this)
    }

    @OnClick(R.id.date_picker_btn, R.id.loading_btn)
    fun onClick(view: View) {
        when (view.id) {
            R.id.date_picker_btn -> showDatePicker()
            R.id.loading_btn -> showLoading()
        }
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
        DatePickerDialog.newInstance(startMillis, endMillis, endMillis, WheelDateUtils.MODE_YMDHM, 0)
            .show(supportFragmentManager, "")
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
