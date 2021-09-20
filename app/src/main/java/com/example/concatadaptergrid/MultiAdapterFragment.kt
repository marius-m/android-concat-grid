package com.example.concatadaptergrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.concatadaptergrid.adapters.ProductAdapterBasic
import com.example.concatadaptergrid.adapters.ProductAdapterSingleHeader
import com.example.concatadaptergrid.adapters.ProductItemFactory
import com.example.concatadaptergrid.databinding.FragmentMultiadapterBinding
import com.example.concatadaptergrid.viewmodels.ProductSimpleUIStateError
import com.example.concatadaptergrid.viewmodels.ProductSimpleUIStateLoading
import com.example.concatadaptergrid.viewmodels.ProductSimpleUIStateSuccess
import com.example.concatadaptergrid.viewmodels.ProductSimpleViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment with limited data
 * Using ConcatAdapter + GridLayoutManager
 */
@AndroidEntryPoint
class MultiAdapterFragment : Fragment() {

    private val vmProduct: ProductSimpleViewModel by viewModels()

    private var _binding: FragmentMultiadapterBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapterProducts1: ProductAdapterBasic
    private lateinit var adapterProducts2: ProductAdapterBasic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiadapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val adapterSingleHeader = ProductAdapterSingleHeader(
            context = context,
            concatAdapterIndex = 0,
            gridSpanSize = PagingFragment.GRID_SPAN_SIZE,
        )
        adapterProducts1 = ProductAdapterBasic(
            context = context,
            concatAdapterIndex = 1,
        )
        val adapterSingleHeader2 = ProductAdapterSingleHeader(
            context = context,
            concatAdapterIndex = 2,
            gridSpanSize = PagingFragment.GRID_SPAN_SIZE,
        )
        adapterProducts2 = ProductAdapterBasic(
            context = context,
            concatAdapterIndex = 3,
        )
        val layoutManager = GridLayoutManager(context, PagingFragment.GRID_SPAN_SIZE)
        val concatAdapterConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        val concatAdapter = ConcatAdapter(
            concatAdapterConfig,
            adapterSingleHeader,
            adapterProducts1,
            adapterSingleHeader2,
            adapterProducts2
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
        adapterSingleHeader.bindHeaderSimple("Items1")
        adapterSingleHeader2.bindHeaderSimple("Items2")
        observeStateChanges()
        vmProduct.fetchProducts()
    }

    private fun observeStateChanges() {
        vmProduct.ldState.observe(this, {
            when (it) {
                is ProductSimpleUIStateError -> {
                    Snackbar
                        .make(binding.root, "Error loading products", Snackbar.LENGTH_SHORT)
                        .show()
                }
                ProductSimpleUIStateLoading -> {
                    Snackbar
                        .make(binding.root, "Loading products", Snackbar.LENGTH_SHORT)
                        .show()
                }
                is ProductSimpleUIStateSuccess -> {
                    Snackbar
                        .make(binding.root, "Showing products", Snackbar.LENGTH_SHORT)
                        .show()
                    adapterProducts1.bindProducts(
                        ProductItemFactory.createFromProducts(it.products1)
                    )
                    adapterProducts2.bindProducts(
                        ProductItemFactory.createFromProducts(it.products2)
                    )
                }
            }.javaClass
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
