package com.example.concatadaptergrid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concatadaptergrid.ConcatenableAdapter
import com.example.concatadaptergrid.databinding.ItemProductCardBinding
import com.example.concatadaptergrid.databinding.ItemProductHeaderBinding

class ProductAdapterBasic(
    private val context: Context,
    override val concatAdapterIndex: Int,
) : RecyclerView.Adapter<ProductVH>(), ConcatenableAdapter {

    private val items = mutableListOf<ProductItemVM>()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ProductVH {
        val localViewType = resolveGlobalViewItemType(viewType)
        return when (ProductViewItemType.fromItemTypeValue(localViewType)) {
            ProductViewItemType.HEADER -> ProductVHHeader(
                binding = ItemProductHeaderBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )
            ProductViewItemType.CARD -> ProductVHCard(
                binding = ItemProductCardBinding.inflate(
                    inflater,
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemViewType = ProductViewItemType
            .toItemType(productItem = items[position])
            .itemTypeValue
        return globalViewItemType(itemViewType)
    }

    override fun onBindViewHolder(viewHolder: ProductVH, position: Int) {
        val item = items[position]
        when (viewHolder) {
            is ProductVHHeader -> viewHolder.bind(item as ProductItemHeader)
            is ProductVHCard -> viewHolder.bind(item as ProductItemCard)
        }.javaClass
    }

    override fun getItemCount(): Int = items.size

    fun bindProducts(
        newProducts: List<ProductItemVM>
    ) {
        this.items.clear()
        this.items.addAll(newProducts)
        notifyDataSetChanged()
    }
}
