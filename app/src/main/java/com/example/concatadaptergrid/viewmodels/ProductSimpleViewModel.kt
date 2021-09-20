package com.example.concatadaptergrid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concatadaptergrid.entities.Product
import com.example.concatadaptergrid.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSimpleViewModel @Inject constructor(
    val productRepository: ProductRepository
) : ViewModel() {

    private val _ldState = MutableLiveData<ProductSimpleUIState>()
    val ldState: LiveData<ProductSimpleUIState>
        get() = _ldState

    fun fetchProducts() {
        viewModelScope.launch {
            _ldState.value = ProductSimpleUIStateLoading
            try {
                val products1 = productRepository.fetchProducts(
                    limit = 20,
                    page = 1
                )
                val products2 = productRepository.fetchProducts(
                    limit = 30,
                    page = 2
                )
                _ldState.value = ProductSimpleUIStateSuccess(products1, products2)
            } catch (e: Exception) {
                _ldState.value = ProductSimpleUIStateError(e.message ?: "error")
            }
        }
    }
}

sealed class ProductSimpleUIState
object ProductSimpleUIStateLoading : ProductSimpleUIState()
data class ProductSimpleUIStateSuccess(
    val products1: List<Product>,
    val products2: List<Product>,
) : ProductSimpleUIState()
data class ProductSimpleUIStateError(val error: String) : ProductSimpleUIState()
