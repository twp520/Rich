package org.amber.rich.data

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import org.amber.rich.home.OrderItem
import java.io.FileOutputStream
import java.io.InputStreamReader
import kotlin.math.max

/**
 * create by colin
 * 2023/5/7
 */
class ProductRepository {

    private val dao = RichDataBaseHolder.getDatabase().productDao()

    suspend fun getProduct(code: String): Product? {
        return dao.queryProductByCode(code)
    }

    fun loadProduct(): PagingSource<Int, Product> {
        return dao.queryProduct()
    }

    suspend fun insertProduct(product: Product) {
        dao.insertProduct(product)
    }

    suspend fun updateProductByOrder(orderList: List<OrderItem>) {
        val productList = mutableListOf<Product>()
        orderList.forEach {
            val updateProduct = it.product.copy(count = max(it.product.count - it.count, 0))
            productList.add(updateProduct)
        }
        if (productList.isNotEmpty())
            dao.updateProduct(productList)
    }

    suspend fun deleteProductByCode(code: String) {
        dao.deleteProductByCode(code)
    }

    suspend fun queryProductByName(key: String): List<Product> {
        if (key.isEmpty()) return emptyList()
        return dao.queryProductByName(key)
    }

    suspend fun saveProductToDevices(contentResolver: ContentResolver, saveUri: Uri) {
        val allProduct = dao.getAllProduct()
        try {
            contentResolver.openFileDescriptor(saveUri, "w")?.use {
                val fos = FileOutputStream(it.fileDescriptor)
                val writer = fos.bufferedWriter()
                allProduct.forEach { product ->
                    writer.write(product.toSavableString())
                    writer.newLine()
                }
                writer.flush()
                fos.close()
                writer.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun loadExistProductFile(contentResolver: ContentResolver, fileUri: Uri) {
        val productList = mutableListOf<Product>()
        contentResolver.openInputStream(fileUri)?.use {
            InputStreamReader(it).forEachLine { lines ->
                // Log.d("loadExistProductFile", " lines -> $lines")
                productList.add(Product.fromSavableString(lines))
            }
        }
        Log.d("loadExistProductFile", "product Size = ${productList.size}")
        if (productList.isNotEmpty()) {
            dao.insertMultipleProduct(productList)
        }
    }


}