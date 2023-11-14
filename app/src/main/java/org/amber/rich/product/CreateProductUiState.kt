package org.amber.rich.product

/**
 * create by colin
 * 2023/5/10
 */
data class CreateProductUiState(
    val code: String = "",
    val title: String = "",
    val count: String = "",
    val price: String = "",
    val cost: String = ""
) {
    val isEditComplete: Boolean
        get() {
            return code.isNotEmpty() && title.isNotEmpty() && count.isNotEmpty() && price.isNotEmpty()
                    && cost.isNotEmpty()
        }
}
