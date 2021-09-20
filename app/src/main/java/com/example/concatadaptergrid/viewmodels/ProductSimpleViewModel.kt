package com.example.concatadaptergrid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.concatadaptergrid.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductSimpleViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _ldState = MutableLiveData<ProductSimpleUIState>()
    val ldState: LiveData<ProductSimpleUIState>
        get() = _ldState
}

sealed class ProductSimpleUIState
object ProductSimpleUIStateLoading : ProductSimpleUIState()
object ProductUIStateSuccess : ProductSimpleUIState()
data class ProductUIStateError(val error: String) : ProductSimpleUIState()
