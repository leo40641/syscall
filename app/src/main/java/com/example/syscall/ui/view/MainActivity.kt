package com.example.syscall.ui.view

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.syscall.ui.viewmodel.CallViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callViewModel : CallViewModel by viewModels()
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        callViewModel.pollingUsb(this, manager)

        callViewModel.callModel.observe(this, Observer { currentCall ->
            println(currentCall.idBell)
            println(currentCall.callOption)
        })

        println("Aqui termino el codigo")
    }
}

