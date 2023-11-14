package org.amber.rich.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * create by colin
 * 2023/9/30
 */
class HomeViewModelFactory(
    private val navToCreateProduct: (code: String) -> Unit,
    private val app: Application
) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(app, navToCreateProduct) as T
        }
        return super.create(modelClass)
    }
}