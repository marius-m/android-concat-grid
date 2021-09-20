package com.example.concatadaptergrid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.concatadaptergrid.adapters.ProductItemCard
import com.example.concatadaptergrid.entities.Product
import com.example.concatadaptergrid.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductPagingViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _ldState = MutableLiveData<PPUiState>()
    val ldState: LiveData<PPUiState>
        get() = _ldState

    val accept: (ProductPagingUiAction) -> Unit
    val state: StateFlow<PPUiState>

    init {
        val actionStateFlow = MutableSharedFlow<ProductPagingUiAction>()
        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
        val searches = actionStateFlow
            .filterIsInstance<ProductPagingUiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(ProductPagingUiAction.Search) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<ProductPagingUiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(ProductPagingUiAction.Scroll) }
        state = searches
            .flatMapLatest { search ->
                combine(
                    queriesScrolled,
                    searchProducts(),
                    ::Pair
                )
            }.map { (scroll, pagingData) ->
                val pagingProductItems = pagingData
                    .map { ProductItemCard.from(it) }
                PPUIStateSuccess(_pagingData = pagingProductItems)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = PPUIStateLoading
            )
    }

    private fun searchProducts(): Flow<PagingData<Product>> =
        productRepository.pagingProductStream()
            .cachedIn(viewModelScope)
}

sealed class PPUiState(
    val pagingData: PagingData<ProductItemCard> = PagingData.empty()
)
object PPUIStateLoading : PPUiState()
data class PPUIStateSuccess(
    private val _pagingData: PagingData<ProductItemCard>
) : PPUiState(_pagingData)

data class PPUIStateError(val error: String) : PPUiState()

sealed class ProductPagingUiAction {
    object Search : ProductPagingUiAction()
    object Scroll : ProductPagingUiAction()
}
