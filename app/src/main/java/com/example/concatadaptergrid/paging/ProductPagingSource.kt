package com.example.concatadaptergrid.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.concatadaptergrid.Tags
import com.example.concatadaptergrid.entities.Product
import com.example.concatadaptergrid.repositories.ProductRepository
import timber.log.Timber
import java.io.IOException

class ProductPagingSource(
    private val productRepository: ProductRepository,
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: START_PAGE
        return try {
            Timber.tag(Tags.INTERNAL).d("Fetching products: page($page) / limit(${params.loadSize})")
            val products: List<Product> = productRepository.fetchProducts(
                page = page,
                limit = params.loadSize,
            )
            Timber.tag(Tags.INTERNAL).d("Products: $products")
            LoadResult.Page(
                data = products,
                prevKey = prevKey(currentPage = page),
                nextKey = nextKey(
                    currentPage = page,
                    currentLoadSize = products.size,
                    requestedItemsToLoad = params.loadSize
                )
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
     * We need to get the previous key (or next key if previous is null) of the page
     * that was closest to the most recently accessed index.
     * Anchor position is the most recently accessed index
     */
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val START_PAGE = 1
        const val PAGE_SIZE = 20

        fun nextKey(
            currentPage: Int,
            currentLoadSize: Int,
            requestedItemsToLoad: Int
        ): Int? {
            return if (currentLoadSize == 0) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                currentPage + (requestedItemsToLoad / PAGE_SIZE)
            }
        }

        fun prevKey(
            currentPage: Int
        ): Int? {
            return if (currentPage == START_PAGE) {
                null
            } else {
                currentPage - 1
            }
        }
    }
}
