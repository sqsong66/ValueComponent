package com.sqsong.valuecomponent.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sqsong.datepicker.utils.DensityUtils
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.bean.City
import com.sqsong.valuecomponent.bean.County
import com.sqsong.valuecomponent.bean.Province
import com.sqsong.valuecomponent.bean.Town
import com.sqsong.valuecomponent.common.OnItemClickListener
import com.sqsong.valuecomponent.db.AddressRoomDatabase
import com.sqsong.valuecomponent.dialog.adapter.ProvinceAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_select_address.*

class SelectAddressDialog : DialogFragment() {

    private var mCompositeDisposable: CompositeDisposable? = null

    private val mCityList = mutableListOf<City>()
    private val mTownList = mutableListOf<Town>()
    private val mCountyList = mutableListOf<County>()
    private val mProvinceList = mutableListOf<Province>()

    private val mProvinceAdapter by lazy {
        ProvinceAdapter(requireContext(), mProvinceList, object : OnItemClickListener<Province> {
            override fun onItemClick(data: Province, position: Int) {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.dialog_select_address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.width = DensityUtils.getScreenWidth()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.windowAnimations = com.sqsong.datepicker.R.style.BottomShowUpDialogStyle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        mCompositeDisposable = CompositeDisposable()

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mProvinceAdapter

        closeIv.setOnClickListener { dismiss() }
        loadProvince()
    }

    private fun loadProvince() {
        mCompositeDisposable?.add(
            Observable.just(true)
                .doOnSubscribe { loadingLayout.visibility = View.VISIBLE }
                .observeOn(Schedulers.io())
                .map {
                    val allProvince = AddressRoomDatabase.getInstance(requireContext()).addressDao()
                        .queryAllProvince()
                    allProvince
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { loadingLayout.visibility = View.GONE }
                .subscribe(
                    {
                        if (it.isNotEmpty()) {
                            mProvinceList.clear()
                            mProvinceList.addAll(it)
                            mProvinceAdapter.notifyDataSetChanged()
                        }
                    },
                    {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }
}