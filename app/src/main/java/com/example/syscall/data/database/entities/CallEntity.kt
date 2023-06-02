package com.example.syscall.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.syscall.domain.model.Call

@Entity(tableName = "call_table")
data class CallEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "idBell") val idBell:Long,
    @ColumnInfo(name = "callOption") val callOption:Int
)

fun Call.toDataBase() = CallEntity(idBell = idBell, callOption = callOption)