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
import com.example.concatadaptergrid.adapters.ProductAdapterSingleHeader
import com.example.concatadaptergrid.databinding.FragmentPagingBinding
import com.example.concatadaptergrid.viewmodels.PPUiState
import com.example.concatadaptergrid.viewmodels.ProductPagingUiAction
import com.example.concatadaptergrid.viewmodels.ProductPagingViewModel
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
class PagingFragment : Fragment() {

    private val vmProduct: ProductPagingViewModel by viewModels()

    private var _binding: FragmentPagingBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val adapterSingleHeader = ProductAdapterSingleHeader(
            context = context,
            concatAdapterIndex = 0,
            gridSpanSize = GRID_SPAN_SIZE,
        )
        val adapterSingleHeader2 = ProductAdapterSingleHeader(
            context = context,
            concatAdapterIndex = 1,
            gridSpanSize = GRID_SPAN_SIZE,
        )
        val adapterPagingProducts = PagingProductAdapter(
            context = context,
            concatAdapterIndex = 2
        )
        val layoutManager = GridLayoutManager(context, GRID_SPAN_SIZE)
        val concatAdapterConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        val concatAdapter = ConcatAdapter(
            concatAdapterConfig,
            adapterSingleHeader,
            adapterSingleHeader2,
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
        adapterSingleHeader.bindHeaderSimple("Single adapter title 1")
        adapterSingleHeader2.bindHeaderSimple("Single adapter title 2")
        binding.bindList(
            adapter = adapterPagingProducts,
            uiState = vmProduct.state,
            onScrollChanged = vmProduct.accept,
        )
    }

    private fun FragmentPagingBinding.bindList(
        adapter: PagingProductAdapter,
        uiState: StateFlow<PPUiState>,
        onScrollChanged: (ProductPagingUiAction.Scroll) -> Unit
    ) {
        productRecycler.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy != 0) {
                        onScrollChanged(ProductPagingUiAction.Scroll)
                    }
                }
            }
        )
        val pagingData = uiState
            .map { it.pagingData }
            .distinctUntilChanged()
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
