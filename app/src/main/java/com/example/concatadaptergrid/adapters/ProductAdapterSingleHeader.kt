package com.example.concatadaptergrid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concatadaptergrid.ConcatenableAdapter
import com.example.concatadaptergrid.databinding.ItemProductHeaderBinding

/**
 * Single item adapter to compose more varied variation of items
 */
class ProductAdapterSingleHeader(
    private val context: Context,
    override val concatAdapterIndex: Int,
    private val gridSpanSize: Int,
) : RecyclerView.Adapter<ProductAdapterSingleHeader.ViewHolder>(), ConcatenableAdapter {

    private val inflater = LayoutInflater.from(context)
    private var productItemHeader: ProductItemHeader = ProductItemHeader.asEmpty()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemProductHeaderBinding.inflate(
                inflater,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.binding.productItemTitle.text = productItemHeader.title
    }

    override fun getItemCount(): Int = 1

    fun bindHeader(productItemHeader: ProductItemHeader) {
        this.productItemHeader = productItemHeader
        notifyDataSetChanged()
    }

    fun bindHeaderSimple(title: String) {
        this.productItemHeader = ProductItemHeader(title = title)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return globalViewItemType()
    }

    override fun spanSizeByType(globalItemViewType: Int): Int {
        return gridSpanSize
    }

    class ViewHolder(
        val binding: ItemProductHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root)

}
