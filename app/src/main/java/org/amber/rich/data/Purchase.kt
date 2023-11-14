package org.amber.rich.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * create by colin
 * 2023/11/1
 *
 *   @ForeignKey(
entity = [Purchase::class],
parentColumns = ["purchaseNumber"],
childColumns = ["purchaseNumber"],
onDelete = ForeignKey.SET_DEFAULT,
onUpdate = ForeignKey.CASCADE
)
 */

/**
 * Purchase 订单表
 *
 * @param purchaseNumber 订单号，主键
 * @param purchasePrice 该订单的总金额
 * @param purchaseDate 该订单的下单时间, 存时间戳
 */
@Entity
data class Purchase(
    @PrimaryKey
    val purchaseNumber: String,
    val purchasePrice: Double,
    val purchaseDate: Long
)

/**
 * @param purchaseId 订单详情ID，主键
 * @param purchaseNumber 外键，与Purchase表中的主键对应，订单号
 * @param productId 外键，与商品的条形码对应
 * @param count 该商品在订单里的数量
 * @param productPrice 该商品在下单的时候的价格
 * @param productCost 该商品在下单的时候的成本价
 */
@Entity
data class PurchaseDetail(
    @PrimaryKey(autoGenerate = true)
    val purchaseId: Long,
    val purchaseNumber: String,
    val productId: String,
    val count: Int,
    val productPrice: Float,
    val productCost: Float,
) {
    fun getSum(): Float = count * productPrice
}


/**
 * 中间类
 * 订单item详情和商品信息
 */
data class PurchaseDetailWithProduct(
    @Embedded val detail: PurchaseDetail,
    @Relation(
        parentColumn = "productId",
        entityColumn = "code",
    )
    val product: Product
)

/**
 * 中间类
 * 订单记录和订单详情列表
 */
data class PurchaseItem(
    @Embedded val purchase: Purchase,
    @Relation(
        entity = PurchaseDetail::class,
        parentColumn = "purchaseNumber",
        entityColumn = "purchaseNumber"
    )
    val detail: List<PurchaseDetailWithProduct>
) {
    fun calculateProfit(): Double {
        val costs = detail.sumOf {
            return@sumOf it.detail.productCost.toDouble() * it.detail.count
        }
        return (purchase.purchasePrice - costs)
    }
}


