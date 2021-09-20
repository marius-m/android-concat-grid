package com.example.concatadaptergrid.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.concatadaptergrid.databinding.ItemProductCardBinding
import com.example.concatadaptergrid.databinding.ItemProductHeaderBinding
import java.lang.IllegalArgumentException

enum class ProductViewItemType(val itemTypeValue: Int) {
    HEADER(0),
    CARD(1),
    ;

    companion object {
        fun toItemType(productItem: ProductItemVM): ProductViewItemType {
            return when (productItem) {
                is ProductItemHeader -> HEADER
                is ProductItemCard -> CARD
                else -> throw IllegalArgumentException("Item card is not supported by the adapter")
            }
        }

        fun fromItemTypeValue(itemTypeValue: Int): ProductViewItemType {
            return values().first { it.itemTypeValue == itemTypeValue }
        }
    }
}

sealed class ProductVH(
    itemView: View,
) : RecyclerView.ViewHolder(itemView)

class ProductVHHeader(
    val binding: ItemProductHeaderBinding
) : ProductVH(binding.root) {
    fun bind(productItem: ProductItemHeader) {
        binding.productItemTitle.text = productItem.title
    }
}

class ProductVHCard(
    val binding: ItemProductCardBinding
) : ProductVH(binding.root) {
    fun bind(productItem: ProductItemCard) {
        binding.productItemTitle.text = productItem.title
        binding.productItemSubtitle.text = productItem.subtitle
        binding.productItemPrice.text = productItem.price
    }
}
