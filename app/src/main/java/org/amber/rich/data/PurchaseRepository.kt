package org.amber.rich.data

import org.amber.rich.home.OrderUiState
import org.amber.rich.purchase.PurchaseDateFilter

/**
 * create by colin
 * 2023/11/2
 */
class PurchaseRepository {

    private val dao = RichDataBaseHolder.getDatabase().purchaseDao()

    suspend fun createPurchaseHistoryFromOlderState(olderUiState: OrderUiState) {
        val timestamp = System.currentTimeMillis()
        val purchaseNumber = "purchase:$timestamp"
        val purchase = Purchase(purchaseNumber, olderUiState.orderSum, timestamp)
        val purchaseDetails = mutableListOf<PurchaseDetail>()
        olderUiState.orderList.forEach {
            purchaseDetails.add(
                PurchaseDetail(
                    0,
                    purchaseNumber,
                    it.product.code,
                    it.count,
                    it.product.price,
                    it.product.cost
                )
            )
        }
        dao.insertPurchase(purchase)
        dao.insertPurchaseDetail(purchaseDetails)
    }

    suspend fun loadPurchase(filter: PurchaseDateFilter): List<PurchaseItem> {
        return dao.queryPurchase(filter.calculateStartTime(), filter.endTime)
    }
}