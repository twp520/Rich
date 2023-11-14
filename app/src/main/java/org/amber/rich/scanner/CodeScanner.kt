package org.amber.rich.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.ServiceListener
import android.bluetooth.BluetoothSocket
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import org.amber.rich.logD
import org.amber.rich.logE
import java.io.IOException

/**
 * create by colin
 * 2023/5/3
 */


object CodeScannerManager {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val scannerScope = MainScope() + SupervisorJob()

    private val _connectState = MutableStateFlow(0)
    val connectState: StateFlow<Int> = _connectState.asStateFlow()

    fun init(context: Context) {
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }


    @SuppressLint("MissingPermission")
    fun getBondDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter.bondedDevices
        //.map {
        //             DeviceData(it.address, it.name)
        //         }
    }


    @SuppressLint("MissingPermission")
    fun startConnect(context: Context, bluetoothDevice: BluetoothDevice) {
        scannerScope.launch(Dispatchers.IO) {
            logD("startConnect deviceData = $bluetoothDevice")
            bluetoothAdapter.cancelDiscovery()
            bluetoothAdapter.getProfileProxy(context, object : ServiceListener {

                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                    logD("onServiceConnected ==>> profile = $profile , proxy = $proxy")
                    if (proxy is BluetoothHidDevice) {

                    }
                }

                override fun onServiceDisconnected(profile: Int) {
                    logE("onServiceDisconnected  profile = $profile")
                }

            }, BluetoothProfile.HID_DEVICE)
        }

    }

    private suspend fun listenData(bluetoothSocket: BluetoothSocket) {
        withContext(Dispatchers.IO) {
            logD("start listen data")
            while (true) {
                if (!bluetoothSocket.isConnected) {
                    break
                }
                try {
                    val readLine = bluetoothSocket.inputStream.bufferedReader().readLine()
                    logD("the scan devices send : $readLine")
                } catch (e: IOException) {
                    logE("listen data has ")
                    e.printStackTrace()
                    break
                }
            }
        }

    }

}