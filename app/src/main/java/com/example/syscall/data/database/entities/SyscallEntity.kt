package com.example.syscall.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_table")
data class SyscallEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "idBell") val idBell:Long,
    @ColumnInfo(name = "callOption") val callOption:Int
)