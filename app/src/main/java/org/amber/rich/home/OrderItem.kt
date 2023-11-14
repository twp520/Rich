package org.amber.rich.home

import org.amber.rich.data.Product

/**
 * create by colin
 * 2023/5/7
 */
data class OrderItem(
    val product: Product,
    var count: Int = 1
) {
    val sum: Float
        get() = product.price * count

    fun getProductCode() = product.code

    override fun equals(other: Any?): Boolean {
        if (other !is OrderItem) return false
        return product == other
    }

    override fun hashCode(): Int {
        return product.hashCode()
    }
}
