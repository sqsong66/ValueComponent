package com.sqsong.valuecomponent.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "province")
data class Province(
    @PrimaryKey val id: String,
    val name: String
)

@Entity(tableName = "city")
data class City(
    @PrimaryKey val id: String,
    val name: String,
    val province: String
)

@Entity(tableName = "county")
data class County(
    @PrimaryKey val id: String,
    val name: String,
    val city: String
)

@Entity(tableName = "town")
data class Town(
    @PrimaryKey val id: String,
    val name: String,
    val city: String
)
