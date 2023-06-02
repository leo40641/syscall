package com.example.syscall.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.syscall.data.database.entities.CallEntity

@Dao
interface SyscallDao {
    @Query("SELECT * FROM call_table ORDER BY id")
    suspend fun getAllCall():List<CallEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(quotes:List<CallEntity>)
}


