package com.example.concatadaptergrid.adapters

import com.example.concatadaptergrid.entities.Product

/**
 * View model class to display product in a card
 */
data class ProductItemCard private constructor(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: String,
) : ProductItemVM {

    companion object {
        fun from(product: Product): ProductItemCard {
            return ProductItemCard(
                id = product.id,
                title = product.title,
                subtitle = "Page(${product.page}) / index(${product.pageItemIndex})",
                price = "${product.price} $",
            )
        }
    }
}
