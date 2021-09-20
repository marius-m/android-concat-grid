package com.example.concatadaptergrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concatadaptergrid.adapters.PagingProductAdapter
import com.example.concatadaptergrid.databinding.FragmentFirstBinding
import com.example.concatadaptergrid.viewmodels.ProductUIState
import com.example.concatadaptergrid.viewmodels.ProductViewModel
import com.example.concatadaptergrid.viewmodels.UiAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    private val vmProduct: ProductViewModel by viewModels()

    private var _binding: FragmentFirstBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val adapterSingleHeader = ProductAdapterSingleHeader(
//            context = context,
//            concatAdapterIndex = 0,
//            gridSpanSize = GRID_SPAN_SIZE,
//        )
//        val adapterSingleNestedCategories = ProductAdapterSingleNestCategories(
//            context = context,
//            concatAdapterIndex = 1,
//            gridSpanSize = GRID_SPAN_SIZE,
//        )
        val adapterPagingProducts = PagingProductAdapter(
            context = requireContext(),
            concatAdapterIndex = 2
        )
        val layoutManager = GridLayoutManager(context, GRID_SPAN_SIZE)
        val concatAdapterConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        val concatAdapter = ConcatAdapter(
            concatAdapterConfig,
//            adapterSingleHeader,
//            adapterSingleNestedCategories,
            adapterPagingProducts
        )
        binding.productRecycler.layoutManager = layoutManager
        binding.productRecycler.adapter = concatAdapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val globalItemViewType = concatAdapter.getItemViewType(position)
                val spanSize: Int = concatAdapter
                    .adapters
                    .filterIsInstance<ConcatenableAdapter>()
                    .first { it.hasGlobalViewItemType(globalItemViewType) }
                    .spanSizeByType(globalItemViewType)
                return spanSize
            }
        }
        binding.bindList(
            adapter = adapterPagingProducts,
            uiState = vmProduct.state,
            onScrollChanged = vmProduct.accept,
        )
    }

    private fun FragmentFirstBinding.bindList(
        adapter: PagingProductAdapter,
        uiState: StateFlow<ProductUIState>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        productRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy != 0) {
                        onScrollChanged(UiAction.Scroll)
                    }
                }
            }
        )
        val pagingData = uiState
            .map { it.pagingData }
            .distinctUntilChanged()
        val loadingState = adapter.loadStateFlow
        lifecycleScope.launch {
            pagingData
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val GRID_SPAN_SIZE = 2
    }
}
