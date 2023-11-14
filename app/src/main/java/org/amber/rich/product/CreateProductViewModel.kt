package org.amber.rich.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.amber.rich.Destinations.NAV_NULL_PATH
import org.amber.rich.data.Product
import org.amber.rich.data.ProductRepository

/**
 * create by colin
 * 2023/5/10
 */
class CreateProductViewModel : ViewModel() {

    private val repository = ProductRepository()
    private val _uiState = MutableStateFlow(CreateProductUiState())
    val uiState: StateFlow<CreateProductUiState> = _uiState.asStateFlow()

    fun initState(productCode: String?) {
        if (productCode == null || productCode == NAV_NULL_PATH)
            return
        viewModelScope.launch(Dispatchers.IO) {
            val product = repository.getProduct(productCode)
            if (product != null) {
                _uiState.update {
                    CreateProductUiState(
                        product.code, product.title,
                        product.count.toString(), product.price.toString(),
                        product.cost.toString()
                    )
                }
            }
        }
    }

    fun saveProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(
                Product(
                    _uiState.value.code.trim('\n', ' '),
                    _uiState.value.price.toFloat(),
                    _uiState.value.cost.toFloat(),
                    _uiState.value.count.toInt(),
                    _uiState.value.title, ""
                )
            )
            _uiState.update {
                CreateProductUiState()
            }
        }
    }

    fun deleteProduct(productCode: String, back: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProductByCode(productCode)
            delay(200)
            withContext(Dispatchers.Main) {
                back()
            }
        }
    }

    fun updateContent(
        code: String = "",
        title: String = "",
        count: String = "",
        price: String = "",
        cost: String = ""
    ) {
        _uiState.update {
            it.copy(
                code = code.ifEmpty { it.code },
                title = title.ifEmpty { it.title },
                count = count.ifEmpty { it.count },
                price = price.ifEmpty { it.price },
                cost = cost.ifEmpty { it.cost }
            )
        }
    }

}