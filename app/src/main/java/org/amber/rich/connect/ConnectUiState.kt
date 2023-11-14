package org.amber.rich.connect

import android.bluetooth.BluetoothDevice

/**
 * create by colin
 * 2023/5/4
 */
data class ConnectUiState(
    val contentState: Int = STATE_LOADING,
    val devicesList: List<BluetoothDevice> = mutableListOf()
) {
    companion object {
        const val STATE_LOADING = 1
        const val STATE_LIST = 2
        const val STATE_CONNECTING = 3
        const val STATE_CONNECTED = 4
    }
}
