package org.amber.rich.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * create by colin
 * 2023/5/7
 */
@Database(
    entities = [Product::class, Purchase::class, PurchaseDetail::class],
    version = 3,
    exportSchema = true,
)
abstract class RichDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun purchaseDao(): PurchaseDao
}
