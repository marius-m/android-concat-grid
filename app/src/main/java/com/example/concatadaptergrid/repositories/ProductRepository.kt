package com.example.concatadaptergrid.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.concatadaptergrid.entities.Product
import com.example.concatadaptergrid.paging.ProductPagingSource
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class ProductRepository {
    suspend fun fetchProducts(
        limit: Int,
        page: Int
    ): List<Product> {
        return (page..(page + limit)).map { index ->
            Product(
                id = "${page}_$index",
                title = "Product",
                page = page,
                pageItemIndex = index,
                price = Random.nextInt(0, 1000).toDouble()
            )
        }
    }

    fun pagingProductStream(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = ProductPagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductPagingSource(this)
            }
        ).flow
    }
}
