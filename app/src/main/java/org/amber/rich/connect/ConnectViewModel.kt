package org.amber.rich.connect

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.amber.rich.scanner.CodeScannerManager
import org.amber.rich.scanner.DeviceData

/**
 * create by colin
 * 2023/5/3
 */
class ConnectViewModel : ViewModel() {

    val state = MutableStateFlow(ConnectUiState())

    fun loadBondDevices() {
        state.update {
            it.copy(devicesList = CodeScannerManager.getBondDevices().toMutableList())
        }
    }

    fun connectDevices(context: Context,bluetoothDevice: BluetoothDevice) {
        CodeScannerManager.startConnect(context,bluetoothDevice)
    }
}