package org.amber.rich.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

/**
 * create by colin
 * 2023/11/2
 */
@Dao
interface PurchaseDao {


    @Transaction
    @Query("SELECT * FROM Purchase WHERE purchaseDate>=:startTime AND purchaseDate<=:endTime ORDER BY purchaseDate DESC")
    suspend fun queryPurchase(startTime: Long, endTime: Long): List<PurchaseItem>

    @Insert
    suspend fun insertPurchase(purchase: Purchase)

    @Insert
    suspend fun insertPurchaseDetail(purchaseDetails: MutableList<PurchaseDetail>)
}