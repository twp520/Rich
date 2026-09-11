package org.amber.rich.purchase

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * create by colin
 * 2023/11/2
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseRoute(back:()->Unit) {

    val viewModel = viewModel<PurchaseViewModel>()
    PurchaseScreen(viewModel,back)
}