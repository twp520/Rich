@file:OptIn(ExperimentalMaterial3Api::class)

package org.amber.rich.home

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import org.amber.rich.Destinations.NAV_NULL_PATH
import org.amber.rich.R
import org.amber.rich.data.CreateFileContract
import org.amber.rich.data.OpenFileContract
import org.amber.rich.data.Product
import org.amber.rich.getString
import org.amber.rich.ui.theme.getTheme
import java.text.SimpleDateFormat
import java.util.Date

/**
 * create by colin
 * 2023/4/28
 */

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navToPurchaseList: () -> Unit,
    navToCreateProduct: (code: String) -> Unit
) {
    val selectedItem = viewModel.selectedItem.collectAsState()
    var showMenu by rememberSaveable { mutableStateOf(false) }
    val theme = getTheme()
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val launchSaveFile = rememberLauncherForActivityResult(
        contract = CreateFileContract(), onResult = { uri ->
            viewModel.exportData(context, uri)
        }
    )
    val launchOpenFile = rememberLauncherForActivityResult(
        contract = OpenFileContract(),
        onResult = { uri ->
            viewModel.loadFile(context, uri)
        }
    )

    val lazyListState = rememberLazyListState()

    Surface(modifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged {
            hasFocus = it.hasFocus
        }
        .focusable()
        .onKeyEvent {
            viewModel.handleOlderKeyEvent(it)
            true
        }) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = theme.primary)
                    .padding(16.dp)
            ) {
                val isRich = selectedItem.value == 0
                val titleString =
                    if (isRich) getString(R.string.cash_register) else
                        getString(R.string.product_list)
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Text(text = titleString, fontSize = 18.sp, color = theme.onPrimary)
                }

                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    val icon = if (isRich) Icons.Filled.List else Icons.Filled.Add
                    if (!isRich) {
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(text = getString(id = R.string.add_product)) },
                                onClick = {
                                    showMenu = false
                                    navToCreateProduct.invoke(NAV_NULL_PATH)
                                })
                            DropdownMenuItem(
                                text = { Text(text = getString(id = R.string.export_data)) },
                                onClick = {
                                    showMenu = false
                                    val time = SimpleDateFormat.getDateTimeInstance()
                                        .format(Date(System.currentTimeMillis()))
                                    launchSaveFile.launch(
                                        context.getString(R.string.export_file_name) + time + ".rich"
                                    )
                                })
                            DropdownMenuItem(
                                text = { Text(text = getString(R.string.import_data)) },
                                onClick = {
                                    showMenu = false
                                    launchOpenFile.launch(arrayOf("*/*"))
                                })
                        }
                    }
                    IconButton(onClick = {
                        if (isRich) {
                            navToPurchaseList.invoke()
                        } else {
                            showMenu = true
                        }
                    }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "The top bar right button",
                            tint = theme.onPrimary
                        )
                    }

                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {

                if (selectedItem.value == 0) {
                    RichPage(viewModel)
                } else {
                    ProductPage(viewModel, lazyListState, navToCreateProduct)
                }
            }

            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem.value == 0,
                    onClick = { viewModel.updateSelectItem(0) },
                    icon = {
                        Icon(
                            Icons.Filled.Home, ""
                        )
                    })
                NavigationBarItem(
                    selected = selectedItem.value == 1,
                    onClick = { viewModel.updateSelectItem(1) },
                    icon = {
                        Icon(
                            Icons.Filled.List, ""
                        )
                    })
            }

        }
        val loadingState = viewModel.loadingState.collectAsState()
        if (loadingState.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                    Text(text = getString(R.string.operating))
                }

            }
        }

    }

    if (!hasFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichPage(viewModel: HomeViewModel) {

    val uiState = viewModel.olderState.collectAsState()
    val context = LocalContext.current

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = context.getString(
                        R.string.older_sum,
                        uiState.value.orderSum
                    ),
                    fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(onClick = { viewModel.cleanOlder() }) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
                Spacer(modifier = Modifier.size(10.dp))
                FloatingActionButton(onClick = { viewModel.completeOlder() }) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = "Done")
                }
            }
        }

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            uiState.value.orderList.forEach {
                item {
                    ListItem(headlineText = { Text(text = it.product.title) },
                        supportingText = {
                            Text(
                                text = context.getString(
                                    R.string.older_item_sub_line,
                                    it.product.price,
                                    it.count
                                )
                            )
                        },
                        trailingContent = {
                            Text(
                                text = context.getString(
                                    R.string.older_item_sum,
                                    it.sum
                                )
                            )
                        })
                }
            }
        }

    }
}

@Composable
fun ProductPage(
    viewModel: HomeViewModel,
    lazyListState: LazyListState,
    navToCreateProduct: (code: String) -> Unit
) {

    val productList = viewModel.productList
    val lazyPagingItems = productList.flow.collectAsLazyPagingItems()
    var searchKey by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val searchProduct = viewModel.searchProductList.collectAsState()

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(text = getString(R.string.hint_search_product)) },
                value = searchKey,
                onValueChange = {
                    searchKey = it
                    viewModel.searchProduct(it)
                })
        }

        if (searchKey.isBlank() || searchKey.isEmpty()) {
            LazyColumn(state = lazyListState) {
                if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .fillMaxHeight()
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                }

                items(count = lazyPagingItems.itemCount) { index ->
                    val item = lazyPagingItems[index]
                    CreateProductItem(
                        context = context,
                        item = item,
                        index = index,
                        itemCount = lazyPagingItems.itemCount,
                        navToCreateProduct = navToCreateProduct
                    )
                }

                if (lazyPagingItems.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        } else {
            LazyColumn {
                items(count = searchProduct.value.size) { index ->
                    val item = searchProduct.value[index]
                    CreateProductItem(
                        context = context,
                        item = item,
                        index = index,
                        itemCount = searchProduct.value.size,
                        navToCreateProduct = navToCreateProduct
                    )
                }
            }
        }
    }

}

@Composable
fun CreateProductItem(
    context: Context,
    item: Product?,
    index: Int,
    itemCount: Int,
    navToCreateProduct: (code: String) -> Unit
) {
    item?.let {
        ListItem(
            headlineText = { Text(text = item.title) },
            supportingText = {
                Text(
                    text = context.getString(
                        R.string.product_item_sub_line,
                        item.price
                    )
                )
            },
            trailingContent = {
                Text(
                    text = context.getString(
                        R.string.product_item_count,
                        item.count
                    )
                )
            },
            modifier = Modifier.clickable {
                navToCreateProduct.invoke(it.code)
            },
            overlineText = { Text(text = item.code) })
        if (index < itemCount - 1) {
            Divider()
        }
    }
}

