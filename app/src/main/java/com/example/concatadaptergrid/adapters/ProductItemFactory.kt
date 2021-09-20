package com.example.concatadaptergrid.adapters

import com.example.concatadaptergrid.entities.Product

object ProductItemFactory {
    fun createFromProducts(
        products: List<Product>
    ): List<ProductItemVM> {
        return products
            .map { ProductItemCard.from(it) }
    }
}
