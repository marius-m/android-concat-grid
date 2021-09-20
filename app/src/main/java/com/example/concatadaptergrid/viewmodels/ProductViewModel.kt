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
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _ldState = MutableLiveData<ProductUIState>()
    val ldState: LiveData<ProductUIState>
        get() = _ldState

    val accept: (UiAction) -> Unit
    val state: StateFlow<ProductUIState>

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll) }
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
                ProductUIStateSuccess(_pagingData = pagingProductItems)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = ProductUIStateLoading
            )
    }

    private fun searchProducts(): Flow<PagingData<Product>> =
        productRepository.pagingProductStream()
            .cachedIn(viewModelScope)
}

sealed class ProductUIState(
    val pagingData: PagingData<ProductItemCard> = PagingData.empty()
)
object ProductUIStateLoading : ProductUIState()
data class ProductUIStateSuccess(
    private val _pagingData: PagingData<ProductItemCard>
) : ProductUIState(_pagingData)

data class ProductUIStateError(val error: String) : ProductUIState()

sealed class UiAction {
    object Search : UiAction()
    object Scroll : UiAction()
}
