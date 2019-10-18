package com.sqsong.valuecomponent.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "province")
@Parcelize
data class Province(
    @PrimaryKey val id: String,
    val name: String
) : Parcelable

@Entity(tableName = "city")
@Parcelize
data class City(
    @PrimaryKey val id: String,
    val name: String,
    val province: String,
    var parentId: String?
) : Parcelable

@Entity(tableName = "county")
@Parcelize
data class County(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    var parentId: String?
) : Parcelable

@Entity(tableName = "town")
@Parcelize
data class Town(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    var parentId: String?
) : Parcelable

@Parcelize
data class AddressData(
    val province: Province?,
    val city: City?,
    val county: County?,
    val town: Town?
) : Parcelable

data class AddressList(
    val provinceList: List<Province>,
    val provinceIndex: Int,
    val cityList: List<City>,
    val cityIndex: Int,
    val countyList: List<County>,
    val countyIndex: Int,
    val townList: List<Town>,
    val townIndex: Int
)