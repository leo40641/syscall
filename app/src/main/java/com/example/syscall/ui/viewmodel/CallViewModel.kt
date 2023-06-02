package com.example.syscall.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.usb.UsbManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.syscall.core.Protocol
import com.example.syscall.domain.model.Call
import com.example.syscall.domain.CallReceiverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(private val usbPort: Protocol, private val callReceiverUseCase: CallReceiverUseCase): ViewModel() {
    val callModel = MutableLiveData<Call>()
    @SuppressLint("SuspiciousIndentation")
    fun pollingUsb(context: Context, usbManager: UsbManager){
        usbPort.connect(context, usbManager)
        viewModelScope.launch {
            while(true) {
                while (!withContext(Dispatchers.IO) { usbPort.listenSyscall() })
                if(usbPort.idBell > 0) {
                    callModel.postValue(Call(idBell = usbPort.idBell, callOption = usbPort.calloption))
                    callReceiverUseCase(listOf(Call(idBell = usbPort.idBell, callOption = usbPort.calloption)))
                    usbPort.cleanCall()
                }
            }
        }
    }

}