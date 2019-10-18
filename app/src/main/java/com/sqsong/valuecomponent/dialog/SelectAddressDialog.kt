package com.sqsong.valuecomponent.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sqsong.datepicker.utils.DensityUtils
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.bean.*
import com.sqsong.valuecomponent.common.OnItemClickListener
import com.sqsong.valuecomponent.db.AddressRoomDatabase
import com.sqsong.valuecomponent.dialog.adapter.CityAdapter
import com.sqsong.valuecomponent.dialog.adapter.CountyAdapter
import com.sqsong.valuecomponent.dialog.adapter.ProvinceAdapter
import com.sqsong.valuecomponent.dialog.adapter.TownAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_select_address.*

interface OnAddressSelectedListener {
    fun onAddressSelected(addressData: AddressData)
}

class SelectAddressDialog : DialogFragment(), View.OnClickListener {

    companion object {
        fun newInstance(addressData: AddressData? = null): SelectAddressDialog {
            return SelectAddressDialog().apply {
                arguments = Bundle().apply {
                    putParcelable("address_data", addressData)
                }
            }
        }
    }

    private var mCompositeDisposable: CompositeDisposable? = null

    private var startX = 0.0f
    private var mCity: City? = null
    private var mTown: Town? = null
    private var mCounty: County? = null
    private var mProvince: Province? = null

    private var mAddressListener: OnAddressSelectedListener? = null

    private var mAddressData: AddressData? = null
    private val mCityList = mutableListOf<City>()
    private val mTownList = mutableListOf<Town>()
    private val mCountyList = mutableListOf<County>()
    private val mProvinceList = mutableListOf<Province>()

    private val mProvinceAdapter by lazy {
        ProvinceAdapter(requireContext(), mProvinceList, object : OnItemClickListener<Province> {
            override fun onItemClick(data: Province, position: Int) {
                mProvince = data
                provinceTv.text = data.name
                cityTv.visibility = View.VISIBLE
                countyTv.visibility = View.GONE
                townTv.visibility = View.GONE

                cityTv.text = getString(R.string.text_please_select)
                translationIndicatorDelay(1)
                loadCity(data.id)
            }
        })
    }

    private val mCityAdapter by lazy {
        CityAdapter(requireContext(), mCityList, object : OnItemClickListener<City> {
            override fun onItemClick(data: City, position: Int) {
                mCity = data
                cityTv.text = data.name
                countyTv.visibility = View.VISIBLE
                townTv.visibility = View.GONE

                countyTv.text = getString(R.string.text_please_select)
                translationIndicatorDelay(2)
                loadCounty(data.id)
            }
        })
    }

    private val mCountyAdapter by lazy {
        CountyAdapter(requireContext(), mCountyList, object : OnItemClickListener<County> {
            override fun onItemClick(data: County, position: Int) {
                mCounty = data
                countyTv.text = data.name
                townTv.visibility = View.VISIBLE

                townTv.text = getString(R.string.text_please_select)
                translationIndicatorDelay(3)
                loadTown(data.id)
            }
        })
    }

    private val mTownAdapter by lazy {
        TownAdapter(requireContext(), mTownList, object : OnItemClickListener<Town> {
            override fun onItemClick(data: Town, position: Int) {
                mTown = data
                townTv.text = data.name
                translationIndicatorDelay(3)
                mAddressListener?.onAddressSelected(AddressData(mProvince, mCity, mCounty, mTown))
                dismiss()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddressData = arguments?.getParcelable("address_data")
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

        closeIv.setOnClickListener(this)
        provinceTv.setOnClickListener(this)
        cityTv.setOnClickListener(this)
        countyTv.setOnClickListener(this)
        townTv.setOnClickListener(this)
        translationIndicatorDelay(0, 10)

        loadInitData()
    }

    private fun loadInitData() {
        val province = mAddressData?.province
        val city = mAddressData?.city
        val county = mAddressData?.county
        val town = mAddressData?.town
        if (province == null || city == null || county == null || town == null) {
            loadProvince()
        } else {
            initAddress(province, city, county, town)
        }
    }

    private fun initAddress(province: Province, city: City, county: County, town: Town) {
        mCompositeDisposable?.add(
            Observable.just(true)
                .doOnSubscribe { loadingLayout.visibility = View.VISIBLE }
                .observeOn(Schedulers.io())
                .map {
                    val addressDao = AddressRoomDatabase.getInstance(requireContext()).addressDao()
                    val allProvince = addressDao.queryAllProvince()
                    var provinceIndex = -1
                    for (p in allProvince) {
                        provinceIndex++
                        if (p.id == province.id) {
                            break
                        }
                    }

                    var cityIndex = -1
                    val cities = addressDao.queryCity(province.id)
                    for (c in cities) {
                        cityIndex++
                        if (c.id == city.id) {
                            break
                        }
                    }

                    var countyIndex = -1
                    val counties = addressDao.queryCounty(city.id)
                    for (c in counties) {
                        countyIndex++
                        if (c.id == county.id) {
                            break
                        }
                    }

                    var townIndex = -1
                    val towns = addressDao.queryTown(county.id)
                    for (t in towns) {
                        townIndex++
                        if (t.id == town.id) {
                            break
                        }
                    }
                    AddressList(
                        allProvince,
                        provinceIndex,
                        cities,
                        cityIndex,
                        counties,
                        countyIndex,
                        towns,
                        townIndex
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { loadingLayout.visibility = View.GONE }
                .subscribe {
                    mProvince = province
                    mCity = city
                    mCounty = county
                    mTown = town

                    provinceTv.text = province.name
                    cityTv.text = city.name
                    cityTv.visibility = View.VISIBLE
                    countyTv.text = county.name
                    countyTv.visibility = View.VISIBLE
                    townTv.text = town.name
                    townTv.visibility = View.VISIBLE

                    mProvinceList.clear()
                    mProvinceList.addAll(it.provinceList)
                    mProvinceAdapter.resetAdapter(it.provinceIndex)

                    mCityList.clear()
                    mCityList.addAll(it.cityList)
                    mCityAdapter.resetAdapter(it.cityIndex)

                    mCountyList.clear()
                    mCountyList.addAll(it.countyList)
                    mCountyAdapter.resetAdapter(it.countyIndex)

                    mTownList.clear()
                    mTownList.addAll(it.townList)
                    mTownAdapter.resetAdapter(it.townIndex)

                    recycler.adapter = mTownAdapter
                    translationIndicatorDelay(3, 10)
                }
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closeIv -> dismiss()
            R.id.provinceTv -> {
                translationIndicatorDelay(0)
                recycler.adapter = mProvinceAdapter
            }
            R.id.cityTv -> {
                translationIndicatorDelay(1)
                recycler.adapter = mCityAdapter
            }
            R.id.countyTv -> {
                translationIndicatorDelay(2)
                recycler.adapter = mCountyAdapter
            }
            R.id.townTv -> {
                translationIndicatorDelay(3)
                recycler.adapter = mTownAdapter
            }
        }
    }

    private fun translationIndicatorDelay(index: Int, time: Long = 300) {
        closeIv.post { translationIndicator(index, time) }
    }

    private fun translationIndicator(index: Int, time: Long) {
        var endX = .0f
        val indicatorWidth = DensityUtils.dip2px(25).toFloat()
        when (index) {
            0 -> {
                endX = provinceTv.left + provinceTv.width * 1.0f / 2 - indicatorWidth / 2
                provinceTv.setTypeface(null, Typeface.BOLD)
                cityTv.setTypeface(null, Typeface.NORMAL)
                countyTv.setTypeface(null, Typeface.NORMAL)
                townTv.setTypeface(null, Typeface.NORMAL)
            }
            1 -> {
                endX = cityTv.left + cityTv.width * 1.0f / 2 - indicatorWidth / 2
                provinceTv.setTypeface(null, Typeface.NORMAL)
                cityTv.setTypeface(null, Typeface.BOLD)
                countyTv.setTypeface(null, Typeface.NORMAL)
                townTv.setTypeface(null, Typeface.NORMAL)
            }
            2 -> {
                endX = countyTv.left + countyTv.width * 1.0f / 2 - indicatorWidth / 2
                provinceTv.setTypeface(null, Typeface.NORMAL)
                cityTv.setTypeface(null, Typeface.NORMAL)
                countyTv.setTypeface(null, Typeface.BOLD)
                townTv.setTypeface(null, Typeface.NORMAL)
            }
            3 -> {
                endX = townTv.left + townTv.width * 1.0f / 2 - indicatorWidth / 2
                provinceTv.setTypeface(null, Typeface.NORMAL)
                cityTv.setTypeface(null, Typeface.NORMAL)
                countyTv.setTypeface(null, Typeface.NORMAL)
                townTv.setTypeface(null, Typeface.BOLD)
            }
        }
        ObjectAnimator.ofFloat(indicatorView, "translationX", startX, endX).apply {
            duration = time
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    startX = endX
                }
            })
            start()
        }
    }

    fun setOnAddressSelectedListener(listener: OnAddressSelectedListener) {
        this.mAddressListener = listener
    }

    private fun loadProvince() {
        mCompositeDisposable?.add(
            Observable.just(true)
                .doOnSubscribe { loadingLayout.visibility = View.VISIBLE }
                .observeOn(Schedulers.io())
                .map {
                    AddressRoomDatabase.getInstance(requireContext()).addressDao()
                        .queryAllProvince()
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

    private fun loadCity(parentId: String) {
        mCompositeDisposable?.add(
            Observable.just(true)
                .doOnSubscribe { loadingLayout.visibility = View.VISIBLE }
                .observeOn(Schedulers.io())
                .map {
                    AddressRoomDatabase.getInstance(requireContext()).addressDao()
                        .queryCity(parentId)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { loadingLayout.visibility = View.GONE }
                .subscribe(
                    {
                        if (it.isNotEmpty()) {
                            mCityList.clear()
                            mCityList.addAll(it)
                            mCityAdapter.resetAdapter()
                            recycler.adapter = mCityAdapter
                        }
                    },
                    {
                        it.printStackTrace()
                    }
                )
        )
    }

    private fun loadCounty(parentId: String) {
        mCompositeDisposable?.add(
            Observable.just(true)
                .doOnSubscribe { loadingLayout.visibility = View.VISIBLE }
                .observeOn(Schedulers.io())
                .map {
                    AddressRoomDatabase.getInstance(requireContext()).addressDao()
                        .queryCounty(parentId)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { loadingLayout.visibility = View.GONE }
                .subscribe(
                    {
                        if (it.isNotEmpty()) {
                            mCountyList.clear()
                            mCountyList.addAll(it)
                            mCountyAdapter.resetAdapter()
                            recycler.adapter = mCountyAdapter
                        }
                    },
                    {
                        it.printStackTrace()
                    }
                )
        )
    }

    private fun loadTown(parentId: String) {
        mCompositeDisposable?.add(
            Observable.just(true)
                .doOnSubscribe { loadingLayout.visibility = View.VISIBLE }
                .observeOn(Schedulers.io())
                .map {
                    AddressRoomDatabase.getInstance(requireContext()).addressDao()
                        .queryTown(parentId)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { loadingLayout.visibility = View.GONE }
                .subscribe(
                    {
                        if (it.isNotEmpty()) {
                            mTownList.clear()
                            mTownList.addAll(it)
                            mTownAdapter.resetAdapter()
                            recycler.adapter = mTownAdapter
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