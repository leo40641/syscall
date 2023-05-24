package com.example.syscall.ui.theme.view

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.syscall.core.Protocol
import com.example.syscall.ui.theme.viewmodel.CallViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callViewModel : CallViewModel by viewModels()
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        callViewModel.pollingUsb(this, manager)


        println("Aqui termino el codigo")
    }
}

