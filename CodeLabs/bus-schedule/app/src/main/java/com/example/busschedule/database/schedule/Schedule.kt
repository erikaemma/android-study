package com.example.busschedule.database.schedule

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(

    @PrimaryKey
    val id: Int,

    // 公交车站名称
    @NonNull
    @ColumnInfo(name="stop_name")
    val stopName: String,

    // 公交到站时间
    // Unix时间戳（Kotlin日期格式）
    @NonNull
    @ColumnInfo(name="arrival_time")
    val arrivalTime: Int,

)