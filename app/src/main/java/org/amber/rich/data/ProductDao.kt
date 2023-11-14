package org.amber.rich.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * create by colin
 * 2023/5/7
 */
@Dao
interface ProductDao {

    @Query("select * from product")
    fun queryProduct(): PagingSource<Int, Product>

    @Query("select * from product where code =:code")
    suspend fun queryProductByCode(code: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: List<Product>)

    @Query("delete from product where code = :code")
    suspend fun deleteProductByCode(code: String)

    @Query("select * from product")
    suspend fun getAllProduct(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleProduct(product: List<Product>)

    @Query("select * from product where title like '%' || :key || '%'")
    suspend fun queryProductByName(key: String): List<Product>
}