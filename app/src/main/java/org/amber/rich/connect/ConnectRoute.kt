package org.amber.rich.connect

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * create by colin
 * 2023/4/28
 */

@Composable
fun ConnectRoute() {
    val viewModel = viewModel<ConnectViewModel>()
    ConnectScreen(viewModel)
}