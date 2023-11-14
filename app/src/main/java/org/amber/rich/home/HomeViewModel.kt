package org.amber.rich.home

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.input.key.KeyEvent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.amber.rich.R
import org.amber.rich.data.Product
import org.amber.rich.data.ProductRepository
import org.amber.rich.data.PurchaseRepository
import org.amber.rich.logD
import org.amber.rich.scanner.KeyCodeHandler

/**
 * create by colin
 * 2023/5/7
 */
class HomeViewModel(app: Application, private val navToCreateProduct: (code: String) -> Unit) :
    AndroidViewModel(app) {

    private val productRepository = ProductRepository()
    private val purchaseRepository = PurchaseRepository()

    private val _olderState = MutableStateFlow(OrderUiState(mutableListOf()))
    val olderState: StateFlow<OrderUiState> = _olderState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    val productList = Pager(PagingConfig(pageSize = 20)) {
        productRepository.loadProduct()
    }

    private val _selectedItemState = MutableStateFlow(0)
    val selectedItem: StateFlow<Int> = _selectedItemState.asStateFlow()

    private val _searchProductList = MutableStateFlow<List<Product>>(emptyList())
    val searchProductList = _searchProductList.asStateFlow()

    private val keyCodeHandler = KeyCodeHandler {
        viewModelScope.launch(Dispatchers.IO) {
            val product = productRepository.getProduct(it)
            logD("getProduct = $product")
            if (product == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        getApplication<Application>().getString(R.string.product_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (selectedItem.value == 0) {
                    //older
                    withContext(Dispatchers.Main) {
                        _olderState.update {
                            it.updateByProduct(product)
                        }
                    }
                } else {
                    //product list
                    withContext(Dispatchers.Main) {
                        navToCreateProduct.invoke(product.code)
                    }
                }
            }
        }
    }


    fun handleOlderKeyEvent(keyEvent: KeyEvent): Boolean {
        return keyCodeHandler.handleKeyEvent(keyEvent)
    }

    val randomProduct = arrayOf(
        "6901028042055",
        "6907992100272",
        "6921311140336",
        "6948960100429",
        "6921168504022",
        "6902538004045",
        "6927984123874"
    )

    fun cleanOlder() {

        keyCodeHandler.inputCallback.invoke(randomProduct.random())
        // _olderState.update {
        //     OrderUiState(mutableListOf())
        // }
    }

    fun completeOlder() {
        //清空页面数据，生成订单数据，更新产品库存
        viewModelScope.launch(Dispatchers.IO) {
            //更新库存
            productRepository.updateProductByOrder(olderState.value.orderList)
            //更新订单历史
            purchaseRepository.createPurchaseHistoryFromOlderState(olderState.value)
            withContext(Dispatchers.Main) {
                _olderState.update {
                    OrderUiState(mutableListOf())
                }
            }
        }
    }

    fun exportData(context: Context, saveUri: Uri?) {
        saveUri ?: return
        viewModelScope.launch {
            _loadingState.update { true }
            withContext(Dispatchers.IO) {
                productRepository.saveProductToDevices(
                    context.applicationContext.contentResolver,
                    saveUri
                )
            }
            delay(1000)
            _loadingState.update { false }
            Toast.makeText(
                context,
                getApplication<Application>().getString(R.string.export_success),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun loadFile(context: Context, fileUri: Uri?) {
        fileUri ?: return
        _loadingState.update { true }
        val contentResolver = context.applicationContext.contentResolver
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.loadExistProductFile(
                    contentResolver,
                    fileUri
                )
            }
            delay(1000)
            _loadingState.update { false }
            Toast.makeText(
                context,
                getApplication<Application>().getString(R.string.import_success),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateSelectItem(position: Int) {
        _selectedItemState.update {
            position
        }
    }


    fun searchProduct(key: String) {
        if (key.isEmpty() || key.isBlank()) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val productByName = productRepository.queryProductByName(key)
                _searchProductList.update {
                    productByName
                }
            }
        }
    }
}