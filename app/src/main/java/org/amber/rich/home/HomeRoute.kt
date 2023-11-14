package org.amber.rich.home

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * create by colin
 * 2023/4/28
 */
@Composable
fun HomeRoute(
    application: Application,
    navToPurchaseList: () -> Unit,
    navToCreateProduct: (code: String) -> Unit
) {


    val viewModel = viewModel<HomeViewModel>(
        factory = HomeViewModelFactory(
            navToCreateProduct,
            application
        )
    )
    HomeScreen(viewModel, navToPurchaseList, navToCreateProduct)
}