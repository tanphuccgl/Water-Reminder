package com.example.waterreminder.database.sound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class SoundEntity (
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "file")
    var file: String,
    @ColumnInfo(name = "rawId")
    var rawId: Int,
    @ColumnInfo(name = "isCheckRaw")
    var isCheckRaw: Int = 1,
    @ColumnInfo(name = "isCheck")
    var isCheck: Boolean = false,

    )