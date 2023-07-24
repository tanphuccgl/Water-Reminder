package com.example.waterreminder.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DrinkWaterRecord")
data class DrinkWaterRecord(
    @PrimaryKey
    @ColumnInfo(name = "image")
    var image: Int,
    @ColumnInfo(name = "time")
    var time :String,
    @ColumnInfo(name = "quantityWater")
    var quantityWater: String
)