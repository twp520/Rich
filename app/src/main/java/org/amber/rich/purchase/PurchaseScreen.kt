@file:OptIn(ExperimentalMaterial3Api::class)

package org.amber.rich.purchase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.amber.rich.R
import org.amber.rich.RichApplication
import org.amber.rich.ui.theme.getTheme


/**
 * create by colin
 * 2023/11/2
 */

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
        TopAppBar(
            title = {
                Text(
                    text = context.getString(
                        R.string.purchase_list_title,
                        filterState.value.title
                    )
                )
            },
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
                IconButton(onClick = {
                    showDatePicker.value = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Filter date range",
                        tint = theme.onPrimary
                    )
                }
                DropdownMenu(
                    expanded = showDatePicker.value,
                    onDismissRequest = { showDatePicker.value = false }) {
                    viewModel.purchaseDateFilters.forEach {
                        DropdownMenuItem(text = { Text(text = it.title) }, onClick = {
                            showDatePicker.value = false
                            viewModel.changeDateFilter(it)
                        })
                    }
                }
            }
        )
    }) { contentPadding ->

        Column(
            modifier = Modifier.padding(
                0.dp,
                contentPadding.calculateTopPadding(),
                0.dp,
                0.dp
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = totalState.value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

            }
            LazyColumn {
                items(listState.value.size) {
                    val itemData = listState.value[it]
                    ListItem(
                        headlineText = {
                            Text(
                                text = context.getString(
                                    R.string.purchase_amount,
                                    itemData.purchase.purchasePrice.toString()
                                )
                            )
                        },
                        overlineText = {
                            Text(
                                text = context.getString(
                                    R.string.purchase_number,
                                    itemData.purchase.purchaseNumber
                                )
                            )
                        },
                        supportingText = {
                            Text(
                                text = context.getString(
                                    R.string.purchase_date,
                                    viewModel.formatDate(itemData.purchase.purchaseDate)
                                )
                            )
                        })
                }
            }
        }

    }

}

