package com.example.waterreminder.database.cup

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "SwitchCup")
data class SwitchCup(
    @ColumnInfo(name = "image")
    var image: Int,
    @PrimaryKey
    @ColumnInfo(name = "quantityWater")
    val quantityWater: String,
    @ColumnInfo(name = "editImage")
    var editImage: Boolean = false,
    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false
                     ) : Serializable