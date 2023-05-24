package com.example.syscall.core

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber

open class Protocol {
    /*Variable de Control de Flujo*/
    var idBell:Long = 0x00

    /*Constantes Tx*/
    val bufferAck = arrayOf(0x02, 0x01, 0x00, 0x08, 0xC5, 0x01, 0x00, 0x2F)
    val bufferAck2 = arrayOf(0x02, 0x01, 0x00, 0x08, 0xC5, 0x05, 0x00, 0x2B)
    /*Constantes Control Rx*/
    val rxArraySize: Int = 512
    val rxFree = 0
    val rxBusy = 1
    val rxWithData = 2
    /*Variable Para Rx*/
    var rxBufferSize: Int = 0
    var rxBuffer = IntArray(rxArraySize)
    var rxControl: Int = 0
    /*Variable Generales*/
    private lateinit var port: UsbSerialPort
    private val readWaitMilis = 25

    @SuppressLint("UnspecifiedImmutableFlag")

    enum class Syscall(val data: Int){
        CONNECTIONCHECK(0XA501),
        MSGBELL(0xA505),
        BUTTONCALL(0x0F),
        BUTTONPAY(0x03),
        BUTTONCLEAR(0x07),
        BELLSINGLE(0x11),
        BELLMULTI(0x12),
    }

    fun connect(context: Context, manager: UsbManager) {
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
        if (availableDrivers.isEmpty()) {
            println("No hay dispositivos")
        } else {
            println("Si hay dispositivos")
            println(availableDrivers[0])
        }
        val driver = availableDrivers[0]
        val connection = manager.openDevice(driver.device)
        val actionUsbPermission = buildString {
            append("com.android.example.USB_PERMISSION")
        }
        val permissionIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(actionUsbPermission),
                PendingIntent.FLAG_IMMUTABLE
            )
        if (connection == null) {
            manager.requestPermission(driver.device, permissionIntent)
            println("No se puedo conectar")
            return
        }
        println("Se pudo conectar")
        port = driver.ports[0] // Most devices have just one port (port 0)
        port.open(connection)
        port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        println("Se pudo configurar el Puerto")
    }

    fun readPort(){
        val buffer = ByteArray(rxArraySize)
        rxControl = rxBusy
        rxBufferSize = port.read(buffer, readWaitMilis)
        rxControl = if(rxBufferSize > 0){
            convertToChar(buffer,rxBufferSize)
            rxWithData
        }else{
            rxFree
        }
    }

    fun writePort(data: Array<Int>) {
        val buffer = ByteArray(data.size)
        for(i in data.indices){
            buffer[i] = data[i].toByte()
        }
        port.write(buffer, 10)
        println("Termino de escribir")
    }

    private fun convertToChar(buffer:ByteArray, size: Int){
        val range = size - 1
        for (i in 0..range) {
            rxBuffer[i] = buffer[i].toInt() and 0xFF
        }
    }

    private fun calcChecksum(buffer:IntArray, size: Int):Int{
        var csum = 0xFF
        val range = size - 2
        for(i in 0..range){
            csum += buffer[i]
        }
        return csum.inv() and 0xFF
    }

    private fun checkChecksum(buffer:IntArray, size: Int):Boolean{
        val range = size - 1
        val csum = calcChecksum(buffer, size)
        return csum == buffer[range]
    }

    fun listenSyscall(): Boolean{
        if (rxControl == rxFree){
            readPort()
            if ((rxControl == rxWithData) && (checkChecksum(rxBuffer, rxBufferSize))){
                when ((rxBuffer[4] shl 8) or rxBuffer[5]){
                    Syscall.CONNECTIONCHECK.data -> {
                        writePort(bufferAck)
                        rxControl = rxFree
                        return true
                    }

                    Syscall.MSGBELL.data -> {
                        idBell = (((rxBuffer[10] shl 24) or (rxBuffer[11] shl 16) or (rxBuffer[12] shl 8) or rxBuffer[13]).toLong()) and 0xFFFFFFFF
                        writePort(bufferAck2)
                        rxControl = rxFree
                        return true
                    }
                }
            }
        }
        return false
    }

}