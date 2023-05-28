package com.example.syscall.ui.theme.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.usb.UsbManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.syscall.core.Protocol
import com.example.syscall.data.model.CallModel
import com.example.syscall.data.model.CallReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(private val usbPort: Protocol): ViewModel() {
    val callModel = MutableLiveData<CallModel>()
    @SuppressLint("SuspiciousIndentation")
    fun pollingUsb(context: Context, usbManager: UsbManager){
        usbPort.connect(context, usbManager)
        viewModelScope.launch {
            while(true) {
                while (!withContext(Dispatchers.IO) { usbPort.listenSyscall() })
                if(usbPort.idBell > 0) {
                    callModel.postValue(CallModel(idBell = usbPort.idBell, callOption = usbPort.calloption))
                    val callReceiver = CallReceiver.dataBellReceiver(CallModel(idBell = usbPort.idBell, callOption = usbPort.calloption))
                    usbPort.cleanCall()
                }
            }
        }
    }

}