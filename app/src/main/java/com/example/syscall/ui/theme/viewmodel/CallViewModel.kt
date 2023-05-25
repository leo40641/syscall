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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallViewModel : ViewModel() {
    val callModel = MutableLiveData<CallModel>()
    private val usbPort = Protocol()

    fun pollingUsb(context: Context, usbManager: UsbManager){
        usbPort.connect(context, usbManager)
        viewModelScope.launch {
            while(true) {
                while (!withContext(Dispatchers.IO) { usbPort.listenSyscall() })
                if(usbPort.idBell > 0) {
                    callModel.postValue(CallModel(idBell = usbPort.idBell, callOption = usbPort.calloption))
                    usbPort.cleanCall()
                }
            }
        }
    }

}