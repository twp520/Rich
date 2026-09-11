package org.amber.rich.purchase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.amber.rich.R
import org.amber.rich.RichApplication
import org.amber.rich.data.PurchaseDetailWithProduct
import org.amber.rich.data.PurchaseItem
import org.amber.rich.ui.theme.getTheme


/**
 * create by colin
 * 2023/11/2
 */

@ExperimentalMaterial3Api
@Composable
fun PurchaseScreen(viewModel: PurchaseViewModel, back: () -> Unit) {

    val listState = viewModel.purchaseState.collectAsState(initial = mutableListOf())
    val filterState = viewModel.filterState.collectAsState()
    val totalState = viewModel.profitState.collectAsState(initial = "")
    val theme = getTheme()
    val showDatePicker = remember {
        mutableStateOf(false)
    }
    val context = RichApplication.appContext


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = context.getString(
                    R.string.purchase_list_title, filterState.value.title
                )
            )
        }, navigationIcon = {
            IconButton(onClick = { back() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = theme.primary,
            titleContentColor = theme.onPrimary,
            navigationIconContentColor = theme.onPrimary
        ), actions = {
            IconButton(onClick = {
                showDatePicker.value = true
            }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Filter date range",
                    tint = theme.onPrimary
                )
            }
            DropdownMenu(expanded = showDatePicker.value,
                onDismissRequest = { showDatePicker.value = false }) {
                viewModel.purchaseDateFilters.forEach {
                    DropdownMenuItem(text = { Text(text = it.title) }, onClick = {
                        showDatePicker.value = false
                        viewModel.changeDateFilter(it)
                    })
                }
            }
        })
    }) { contentPadding ->

        var showBottomSheet by remember { mutableStateOf(false) }
        var selectedPurchase by remember { mutableStateOf<PurchaseItem?>(null) }
        Column(
            modifier = Modifier.padding(
                0.dp, contentPadding.calculateTopPadding(), 0.dp, 0.dp
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = totalState.value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

            }
            LazyColumn {
                items(listState.value.size) {
                    val itemData = listState.value[it]
                    ListItem(headlineContent = {
                        Text(
                            text = context.getString(
                                R.string.purchase_amount,
                                itemData.purchase.purchasePrice.toString()
                            )
                        )
                    }, overlineContent = {
                        Text(
                            text = context.getString(
                                R.string.purchase_number, itemData.purchase.purchaseNumber
                            )
                        )
                    }, supportingContent = {
                        Text(
                            text = context.getString(
                                R.string.purchase_date,
                                viewModel.formatDate(itemData.purchase.purchaseDate)
                            )
                        )
                    }, modifier = Modifier.clickable {
                        selectedPurchase = itemData
                        showBottomSheet = true
                    })
                }
            }
        }
        if (showBottomSheet && selectedPurchase != null) {
            PurchaseDetailBottomSheet(selectedPurchase,
                viewModel,
                onDismiss = {
                    showBottomSheet = false
                    selectedPurchase = null
                })
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun PurchaseDetailBottomSheet(
    purchaseItem: PurchaseItem?,
    viewModel: PurchaseViewModel,
    onDismiss: () -> Unit
) {
    purchaseItem ?: return
    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "订单详情",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 订单概览信息
            val purchase = purchaseItem.purchase

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("订单号:", fontWeight = FontWeight.Bold)
                    Text(purchase.purchaseNumber)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("总金额:", fontWeight = FontWeight.Bold)
                    Text("¥${purchase.purchasePrice}")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("下单时间:", fontWeight = FontWeight.Bold)
                    Text(viewModel.formatDate(purchase.purchaseDate))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("预计利润:", fontWeight = FontWeight.Bold)
                    Text("¥${purchaseItem.calculateProfit().format(2)}")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // 商品列表标题
            Text(
                text = "商品清单",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // 商品列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(purchaseItem.detail) { detailWithProduct ->
                    PurchaseDetailItem(detailWithProduct)
                }
            }

        }
    }


}

@Composable
fun PurchaseDetailItem(detailWithProduct: PurchaseDetailWithProduct) {
    val detail = detailWithProduct.detail
    val product = detailWithProduct.product

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 商品名称和条形码
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "条形码: ${product.code}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 价格和数量信息
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("单价", fontSize = 12.sp)
                    Text("¥${detail.productPrice.format(2)}")
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("数量", fontSize = 12.sp)
                    Text("${detail.count}")
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("小计", fontSize = 12.sp)
                    Text("¥${detail.getSum().format(2)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 扩展函数：格式化Double为带两位小数的字符串
fun Double.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}

// 扩展函数：格式化Float为带两位小数的字符串
fun Float.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}

