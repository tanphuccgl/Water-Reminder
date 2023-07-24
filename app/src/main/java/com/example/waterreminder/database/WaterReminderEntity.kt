package com.example.waterreminder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "WaterReminder")
class WaterReminderEntity (
    @ColumnInfo(name = "image")
    var image: Int,
    @ColumnInfo(name = "time")
    var time :String,
    @ColumnInfo(name = "quantityWater")
    var quantityWater: String,
    @PrimaryKey
    @ColumnInfo (name = "checkedDate")
    val checkedDate : String
    )