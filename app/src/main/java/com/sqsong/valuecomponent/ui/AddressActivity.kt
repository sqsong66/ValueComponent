package com.sqsong.valuecomponent.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sqsong.valuecomponent.R
import com.sqsong.valuecomponent.bean.City
import com.sqsong.valuecomponent.bean.County
import com.sqsong.valuecomponent.bean.Province
import com.sqsong.valuecomponent.bean.Town
import com.sqsong.valuecomponent.db.AddressRoomDatabase
import com.sqsong.valuecomponent.dialog.LoadingDialog
import com.sqsong.valuecomponent.dialog.SelectAddressDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_address.*
import java.io.BufferedReader
import java.io.InputStreamReader

class AddressActivity : AppCompatActivity(), View.OnClickListener {

    private var mLoadingDialog: LoadingDialog? = null
    private var mRxPermissions: RxPermissions? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        initEvent()
    }

    private fun initEvent() {
        mCompositeDisposable = CompositeDisposable()
        mRxPermissions = RxPermissions(this)
        insertBtn.setOnClickListener(this)
        showAddressBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.insertBtn -> {
                mRxPermissions?.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.subscribe {
                        if (it) {
                            insertDataToDB()
                        } else {
                            Toast.makeText(
                                this,
                                "Please allow SDCard permission.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            R.id.showAddressBtn -> checkDataDB()
        }
    }

    private fun checkDataDB() {
        mCompositeDisposable?.add(Observable.just(true)
            .map {
                AddressRoomDatabase.getInstance(applicationContext).addressDao()
                    .queryProvinceCount()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it < 1) {
                    Toast.makeText(
                        this,
                        "Please insert data to database first.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showAddressDialog()
                }
            }
        )
    }

    private fun showAddressDialog() {
        SelectAddressDialog().show(supportFragmentManager, "")
    }

    private fun insertDataToDB() {
        mCompositeDisposable?.add(Observable.just(true)
            .doOnSubscribe {
                if (mLoadingDialog == null) {
                    mLoadingDialog = LoadingDialog()
                }
                mLoadingDialog?.show(supportFragmentManager, "")
            }
            .observeOn(Schedulers.io())
            .map { analysisJsonToDB() }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { mLoadingDialog?.dismiss() }
            .subscribe(
                { Toast.makeText(this, "Insert success~", Toast.LENGTH_SHORT).show() },
                {
                    it.printStackTrace()
                    Toast.makeText(this, "Insert failed!", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

    private fun analysisJsonToDB() {
        val gson = Gson()
        val addressDao = AddressRoomDatabase.getInstance(applicationContext).addressDao()

        // Province
        val provinceType = object : TypeToken<List<Province>>() {}.type
        val provinceReader = BufferedReader(InputStreamReader(assets.open("province.json")))
        val provinceList = gson.fromJson<List<Province>>(provinceReader, provinceType)
        if (provinceList.isNotEmpty()) {
            addressDao.clearProvinceData()
            addressDao.insertProvinceList(provinceList)
        }

        // City
        val cityType = object : TypeToken<LinkedHashMap<String, List<City>>>() {}.type
        val cityReader = BufferedReader(InputStreamReader(assets.open("city.json")))
        val cityMap = gson.fromJson<LinkedHashMap<String, List<City>>>(cityReader, cityType)
        if (cityMap.isNotEmpty()) {
            addressDao.clearCityData()
            for ((_, value) in cityMap) {
                addressDao.insertCityList(value)
            }
        }

        // County
        val countyType = object : TypeToken<LinkedHashMap<String, List<County>>>() {}.type
        val countyReader = BufferedReader(InputStreamReader(assets.open("county.json")))
        val countyMap = gson.fromJson<LinkedHashMap<String, List<County>>>(countyReader, countyType)
        if (countyMap.isNotEmpty()) {
            addressDao.clearCountyData()
            for ((_, value) in countyMap) {
                addressDao.insertCountyList(value)
            }
        }
        val allCounty = addressDao.queryAllCounties()
        Log.e("sqsong", "County size: ${allCounty.size}")

        // Town
        val townType = object : TypeToken<LinkedHashMap<String, List<Town>>>() {}.type
        val townReader = BufferedReader(InputStreamReader(assets.open("town.json")))
        val townMap = gson.fromJson<LinkedHashMap<String, List<Town>>>(townReader, townType)
        if (townMap.isNotEmpty()) {
            addressDao.clearTownData()
            for ((_, value) in townMap) {
                addressDao.insertTownList(value)
            }
        }

        val town = addressDao.queryTown("460400500000")
        Log.i("sqsong", "Town Name --> ${town.name}, City --> ${town.city}, Id -> ${town.id}")
    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }
}