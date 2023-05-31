package com.example.syscall.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.syscall.data.database.dao.SyscallDao
import com.example.syscall.data.database.entities.SyscallEntity

@Database(entities = [SyscallEntity::class], version = 1)
abstract class SyscallDataBase: RoomDatabase() {

    abstract fun getSyscallDao(): SyscallDao
}