package com.example.concatadaptergrid.adapters

/**
 * View model class to display product in a header
 */
data class ProductItemHeader(
    val title: String,
) : ProductItemVM {
    companion object {
        fun asEmpty(): ProductItemHeader {
            return ProductItemHeader(
                title = "",
            )
        }
    }
}
