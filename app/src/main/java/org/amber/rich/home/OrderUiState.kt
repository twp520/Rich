package org.amber.rich.home

import org.amber.rich.data.Product

/**
 * create by colin
 * 2023/5/7
 */
data class OrderUiState(
    val orderList: MutableList<OrderItem>,
) {
    val orderSum: Double
        get() {
            if (orderList.isEmpty()) return 0.0
            return orderList.sumOf { it.sum.toDouble() }
        }

    fun updateByProduct(product: Product): OrderUiState {
        val newOlderItem = OrderItem(product, 1)
        val indexOfFirst = orderList.indexOfFirst {
            it.getProductCode() == product.code
        }
        if (indexOfFirst >= 0) {
            val olderItem = orderList[indexOfFirst]
            olderItem.count += 1
        } else {
            orderList.add(newOlderItem)
        }
        return copy(orderList = mutableListOf<OrderItem>().apply { addAll(orderList) })
    }
}
