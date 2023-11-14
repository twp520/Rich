package org.amber.rich.purchase

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * create by colin
 * 2023/11/2
 */

@Composable
fun PurchaseRoute(back:()->Unit) {

    val viewModel = viewModel<PurchaseViewModel>()
    PurchaseScreen(viewModel,back)
}