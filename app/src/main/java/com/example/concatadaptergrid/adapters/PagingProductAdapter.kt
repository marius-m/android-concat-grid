package com.example.concatadaptergrid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.concatadaptergrid.ConcatenableAdapter
import com.example.concatadaptergrid.databinding.ItemProductCardBinding
import com.example.concatadaptergrid.databinding.ItemProductHeaderBinding

class PagingProductAdapter(
    context: Context,
    override val concatAdapterIndex: Int,
) : PagingDataAdapter<ProductItemCard, ProductVH>(REPO_COMPARATOR),
    ConcatenableAdapter {

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

    override fun onBindViewHolder(viewHolder: ProductVH, position: Int) {
        val item = getItem(position)
        when (viewHolder) {
            is ProductVHHeader -> viewHolder.bind(item as ProductItemHeader)
            is ProductVHCard -> viewHolder.bind(item as ProductItemCard)
        }.javaClass
    }

    override fun getItemViewType(position: Int): Int {
        val itemViewType = ProductViewItemType
            .CARD
            .itemTypeValue
        return globalViewItemType(itemViewType)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ProductItemCard>() {
            override fun areItemsTheSame(
                oldItem: ProductItemCard,
                newItem: ProductItemCard
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductItemCard,
                newItem: ProductItemCard
            ): Boolean =
                oldItem == newItem
        }
    }
}
