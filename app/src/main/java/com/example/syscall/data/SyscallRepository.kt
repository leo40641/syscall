package com.example.syscall.data

import com.example.syscall.data.database.dao.SyscallDao
import com.example.syscall.data.database.entities.CallEntity
import javax.inject.Inject

class SyscallRepository @Inject constructor(
    private val syscallDao:SyscallDao
) {
    suspend fun insertCall(call:List<CallEntity>){
        syscallDao.insertCall(call)
    }
}