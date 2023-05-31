package com.example.syscall.data

import com.example.syscall.data.database.dao.SyscallDao
import com.example.syscall.data.database.entities.SyscallEntity
import javax.inject.Inject

class SyscallRepository @Inject constructor(
    private val syscallDao:SyscallDao
) {
    suspend fun insertCall(call:List<SyscallEntity>){
        syscallDao.insertCall(call)
    }
}