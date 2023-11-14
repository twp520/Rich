package org.amber.rich.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

/**
 * create by colin
 * 2023/5/7
 */

@Entity
data class Product(
    @PrimaryKey
    val code: String,
    val price: Float = 0f,
    val cost: Float = 0f,
    val count: Int,
    val title: String,
    val pic: String
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Product) return false
        return code == other.code
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }

    fun toSavableString(): String {
        return JSONObject().put("code", code)
            .put("price", price)
            .put("cost", cost)
            .put("count", count)
            .put("title", title)
            .put("pic", pic)
            .toString()
    }

    companion object {
        fun fromSavableString(json: String): Product {
            val obj = JSONObject(json)
            return Product(
                obj.optString("code"),
                obj.optString("price").toFloat(),
                obj.optString("cost", "0").toFloat(),
                obj.optInt("count"),
                obj.optString("title"),
                obj.optString("pic")
            )
        }
    }
}