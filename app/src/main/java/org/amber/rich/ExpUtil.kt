package org.amber.rich

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * create by colin
 * 2023/5/3
 */

fun logD(msg: String) {
    Log.d("rich", msg)
}

fun logE(msg: String) {
    Log.e("rich", msg)
}

@Composable
fun getString(id: Int): String {
    return LocalContext.current.getString(id)
}


