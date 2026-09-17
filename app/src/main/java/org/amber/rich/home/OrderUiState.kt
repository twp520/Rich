package org.amber.rich.home

import org.amber.rich.data.Product

/**
 * create by colin
 * 2023/5/7
 */
data class OrderUiState(
    val orderList: List<OrderItem>,
) {
    val orderSum: Double
        get() {
            if (orderList.isEmpty()) return 0.0
            return orderList.sumOf { it.sum.toDouble() }
        }

    fun updateByProduct(product: Product): OrderUiState {
        val indexOfFirst = orderList.indexOfFirst {
            it.getProductCode() == product.code
        }
        val newOrderList = orderList.toMutableList()
        if (indexOfFirst >= 0) {
            val olderItem = orderList[indexOfFirst]
            newOrderList[indexOfFirst] = olderItem.copy(count = olderItem.count + 1)
        } else {
            newOrderList.add(OrderItem(product, 1))
        }
        return copy(orderList = newOrderList.toList())
    }
}
