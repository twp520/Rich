package org.amber.rich.product

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * create by colin
 * 2023/5/8
 */

@Composable
fun CreateProductRoute(productCode: String?, back: () -> Unit) {
    val viewModel = viewModel<CreateProductViewModel>()
    CreateProductScreen(viewModel, productCode, back)
}