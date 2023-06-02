package com.example.syscall.domain

import com.example.syscall.data.SyscallRepository
import com.example.syscall.data.database.entities.toDataBase
import com.example.syscall.domain.model.Call
import javax.inject.Inject

class CallReceiverUseCase @Inject constructor(private val repository: SyscallRepository) {
    suspend operator fun invoke(call:List<Call>){
        repository.insertCall(call.map {it.toDataBase()  })
    }
}