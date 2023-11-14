package org.amber.rich.product

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.amber.rich.Destinations.NAV_NULL_PATH
import org.amber.rich.R
import org.amber.rich.getString
import org.amber.rich.ui.theme.getTheme

/**
 * create by colin
 * 2023/5/8
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(viewModel: CreateProductViewModel, productCode: String?, back: () -> Unit) {

    val productUiState = viewModel.uiState.collectAsState()
    val theme = getTheme()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    viewModel.initState(productCode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.product_edit)) },
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = theme.primary,
                    titleContentColor = theme.onPrimary,
                    navigationIconContentColor = theme.onPrimary
                ),
                actions = {
                    if (productCode != null && productCode != NAV_NULL_PATH) {
                        IconButton(onClick = {
                            viewModel.deleteProduct(productCode, back)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = theme.onPrimary
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (productUiState.value.isEditComplete) {
                    viewModel.saveProduct()
                    focusRequester.requestFocus()
                }
            }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done")
            }
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = 16.dp,
                        top = contentPadding.calculateTopPadding(),
                        end = 16.dp
                    )
                )
        ) {
            CreateInputText(
                value = productUiState.value.code,
                valueChang = { viewModel.updateContent(code = it) },
                label = context.getString(R.string.product_code),
                Modifier
                    .focusable()
                    .focusRequester(focusRequester),
                KeyboardType.Text
            )

            CreateInputText(
                value = productUiState.value.title,
                valueChang = { viewModel.updateContent(title = it) },
                label = context.getString(R.string.product_title),
                Modifier.focusable(),
                keyboardType = KeyboardType.Text
            )
            CreateInputText(
                value = productUiState.value.cost,
                valueChang = { viewModel.updateContent(cost = it) },
                label = getString(R.string.product_cost),
                modifier = Modifier.focusable()
            )
            CreateInputText(
                value = productUiState.value.price,
                valueChang = { viewModel.updateContent(price = it) },
                label = context.getString(R.string.product_price),
                Modifier.focusable()
            )
            CreateInputText(
                value = productUiState.value.count,
                valueChang = { viewModel.updateContent(count = it) },
                label = context.getString(R.string.product_count),
                Modifier.focusable()
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInputText(
    value: String,
    valueChang: (value: String) -> Unit,
    label: String,
    modifier: Modifier,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    OutlinedTextField(
        modifier = Modifier
            .then(modifier)
            .padding(top = 30.dp),
        value = value,
        onValueChange = {
            if (keyboardType == KeyboardType.Number) {
                if (it.matches(Regex("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])"))) {
                    valueChang(it)
                }
            } else {
                valueChang(it)
            }
        },
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = value.isEmpty(),
    )
}