package com.sqsong.valuecomponent.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sqsong.valuecomponent.bean.City
import com.sqsong.valuecomponent.bean.County
import com.sqsong.valuecomponent.bean.Province
import com.sqsong.valuecomponent.bean.Town

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProvince(province: Province)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProvinceList(provinceList: List<Province>)

    @Query("select * from province")
    fun queryAllProvince(): List<Province>

    @Query("delete from province")
    fun clearProvinceData()

    @Query("select count(*) from province")
    fun queryProvinceCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCityList(cityList: List<City>)

    @Query("select * from city")
    fun queryAllCities(): List<City>

    @Query("delete from city")
    fun clearCityData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountyList(countyList: List<County>)

    @Query("select * from county")
    fun queryAllCounties(): List<County>

    @Query("delete from county")
    fun clearCountyData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTown(town: Town): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTownList(townList: List<Town>)

    @Query("select * from town")
    fun queryAllTowns(): List<Town>

    @Query("delete from town")
    fun clearTownData()

    @Query("select * from town where id = :id")
    fun queryTown(id: String): Town
}