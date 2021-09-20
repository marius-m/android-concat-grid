package com.example.concatadaptergrid

/**
 * Identifies adapter items that it will be used in [ConcatAdapter]
 * This class functionality creates adapter itemViewType unique for all adapters
 * This is useful when used in [ConcatAdapter] + [ConcatAdapter.setIsolateViewTypes(false)]
 * Be sure to provide [globalViewItemType] when identifying an item in child adapter, and
 * restore itemViewType back when used internally
 */
interface ConcatenableAdapter {
    val concatAdapterIndex: Int

    /**
     * Returns span size when used in Grid
     * Span size is resolved in numbers.
     * - Grid spanCount=2
     *   - When takes 1 column out of 2, spanSize = 1
     *   - When takes 2 column out of 2, spanSize = 1
     *   - When takes both columns 2, spanSize = 2
     * By default this does not change span size
     * @param globalItemViewType global item view type (calculated with [resolveGlobalViewItemType])
     * @return span size
     */
    fun spanSizeByType(globalItemViewType: Int): Int = 1

    /**
     * @return true if item type belongs to adapter
     */
    fun hasGlobalViewItemType(globalItemViewType: Int): Boolean {
        val minItemIndex = VIEW_ITEM_TYPE_MULTIPLIER * (concatAdapterIndex + 1)
        val maxItemIndex = VIEW_ITEM_TYPE_MULTIPLIER * (concatAdapterIndex + 2)
        return globalItemViewType >= minItemIndex &&
            globalItemViewType < maxItemIndex
    }

    /**
     * @return [RecyclerView.Adapter.getItemViewType(position: Int)] when used in
     * [ConcatAdapter] to provide a unique item type
     */
    fun globalViewItemType(localItemViewType: Int = 0): Int {
        return VIEW_ITEM_TYPE_MULTIPLIER * (concatAdapterIndex + 1) + localItemViewType
    }

    /**
     * Returns the original view item type for internal use
     * @param globalItemViewType is calculated type with [globalViewItemType]
     * @return resolved local itemViewType
     */
    fun resolveGlobalViewItemType(globalItemViewType: Int): Int {
        return globalItemViewType - (VIEW_ITEM_TYPE_MULTIPLIER * (concatAdapterIndex + 1))
    }

    companion object {
        private const val VIEW_ITEM_TYPE_MULTIPLIER = 100
    }
}
