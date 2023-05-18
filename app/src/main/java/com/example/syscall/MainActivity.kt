package com.example.syscall

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.syscall.ui.theme.SyscallTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val usbPrueba = Protocol()
        usbPrueba.connect(this, manager)
        lifecycleScope.launch {
            while(true) {
                while (!withContext(Dispatchers.IO) { usbPrueba.listenSyscall() }) {
                    println("No Hay Datos")
                }
                println("Si hay datos")
            }
        }
        println("Aqui termino el codigo")
    }
}

